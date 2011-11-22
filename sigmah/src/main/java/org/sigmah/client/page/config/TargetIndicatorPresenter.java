package org.sigmah.client.page.config;

import java.util.List;

import org.sigmah.client.AppEvents;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.UIConstants;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.common.grid.AbstractEditorGridPresenter;
import org.sigmah.client.page.common.grid.TreeGridView;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.design.AttributeGroupFolder;
import org.sigmah.client.page.config.design.IndicatorFolder;

import org.sigmah.client.util.state.StateProvider;
import org.sigmah.shared.command.BatchCommand;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.UpdateEntity;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;

public class TargetIndicatorPresenter extends AbstractEditorGridPresenter<ModelData> {

	@ImplementedBy(TargetIndicatorView.class)
	public interface View extends TreeGridView<TargetIndicatorPresenter, ModelData> {
		public void init(TargetIndicatorPresenter presenter, UserDatabaseDTO db, TreeStore store);
	}

	private final EventBus eventBus;
	private final Dispatcher service;
	private final View view;
	private final UIConstants messages;

	private UserDatabaseDTO db;
	private TreeStore<ModelData> treeStore;

	@Inject
	public TargetIndicatorPresenter(EventBus eventBus, Dispatcher service,
			StateProvider stateMgr, View view, UIConstants messages) {
		super(eventBus, service, stateMgr, view);
		this.eventBus = eventBus;
		this.service = service;
		this.view = view;
		this.messages = messages;
	}

	public void go(UserDatabaseDTO db) {

		this.db = db;

		treeStore = new TreeStore<ModelData>();
		fillStore(messages);

		initListeners(treeStore, null);

		this.view.init(this, db, treeStore);
		this.view.setActionEnabled(UIActions.delete, false);
	}

	private void fillStore(UIConstants messages) {
		for (ActivityDTO activity : db.getActivities()) {
            ActivityDTO activityNode = new ActivityDTO(activity);
            treeStore.add(activityNode, false);

            AttributeGroupFolder attributeFolder = new AttributeGroupFolder(activityNode, messages.attributes());
            treeStore.add(activityNode, attributeFolder, false);

            for (AttributeGroupDTO group : activity.getAttributeGroups()) {
            	if (group != null) {
	                AttributeGroupDTO groupNode = new AttributeGroupDTO(group);
	                treeStore.add(attributeFolder, groupNode, false);
	
	                for (AttributeDTO attribute : group.getAttributes()) {
	                    AttributeDTO attributeNode = new AttributeDTO(attribute);
	                    treeStore.add(groupNode, attributeNode, false);
	                }
            	}
            }

            IndicatorFolder indicatorFolder = new IndicatorFolder(activityNode, messages.indicators());
            treeStore.add(activityNode, indicatorFolder, false);

            for (IndicatorDTO indicator : activity.getIndicators()) {
                IndicatorDTO indicatorNode = new IndicatorDTO(indicator);
                treeStore.add(indicatorFolder, indicatorNode, false);
            }
        }
	}

	@Override
	public Store<ModelData> getStore() {
		return treeStore;
	}

	public TreeStore<ModelData> getTreeStore() {
		return treeStore;
	}

	public void onNodeDropped(ModelData source) {

		// update sortOrder

		ModelData parent = treeStore.getParent(source);
		List<ModelData> children = parent == null ? treeStore.getRootItems()
				: treeStore.getChildren(parent);

		for (int i = 0; i != children.size(); ++i) {
			Record record = treeStore.getRecord(children.get(i));
			record.set("sortOrder", i);
		}

	}

	public void onNew(String entityName) {

		final EntityDTO newEntity;
		ModelData parent;

		ModelData selected = view.getSelection();

		if ("Activity".equals(entityName)) {
			newEntity = new ActivityDTO(db);
			newEntity.set("databaseId", db.getId());
			parent = null;

		} else if ("AttributeGroup".equals(entityName)) {
			ActivityDTO activity = findActivityFolder(selected);

			newEntity = new AttributeGroupDTO();
			newEntity.set("activityId", activity.getId());
			parent = treeStore.getChild(activity, 0);

		} else if ("Attribute".equals(entityName)) {
			AttributeGroupDTO group = findAttributeGroupNode(selected);

			newEntity = new AttributeDTO();
			newEntity.set("attributeGroupId", group.getId());

			parent = group;

		} else if ("Indicator".equals(entityName)) {
			ActivityDTO activity = findActivityFolder(selected);

			IndicatorDTO newIndicator = new IndicatorDTO();
			newIndicator.setAggregation(IndicatorDTO.AGGREGATE_SUM);

			newEntity = newIndicator;
			newEntity.set("activityId", activity.getId());

			parent = treeStore.getChild(activity, 1);

		} else {
			return; // TODO log error
		}

		createEntity(parent, newEntity);
	}

	private void createEntity(final ModelData parent, final EntityDTO newEntity) {

	}

	protected ActivityDTO findActivityFolder(ModelData selected) {

		while (!(selected instanceof ActivityDTO)) {
			selected = treeStore.getParent(selected);
		}

		return (ActivityDTO) selected;
	}

	protected AttributeGroupDTO findAttributeGroupNode(ModelData selected) {
		if (selected instanceof AttributeGroupDTO) {
			return (AttributeGroupDTO) selected;
		}
		if (selected instanceof AttributeDTO) {
			return (AttributeGroupDTO) treeStore.getParent(selected);
		}
		throw new AssertionError("not a valid selection to add an attribute !");

	}

	@Override
	protected void onDeleteConfirmed(final ModelData model) {
		service.execute(new Delete((EntityDTO) model),
				view.getDeletingMonitor(), new AsyncCallback<VoidResult>() {
					public void onFailure(Throwable caught) {

					}

					public void onSuccess(VoidResult result) {
						treeStore.remove(model);
						eventBus.fireEvent(AppEvents.SchemaChanged);
					}
				});
	}

	@Override
	protected String getStateId() {
		return "target" + db.getId();
	}

	@Override
	protected Command createSaveCommand() {
		BatchCommand batch = new BatchCommand();

		for (ModelData model : treeStore.getRootItems()) {
			prepareBatch(batch, model);
		}
		return batch;
	}

	protected void prepareBatch(BatchCommand batch, ModelData model) {
		if (model instanceof EntityDTO) {
			Record record = treeStore.getRecord(model);
			if (record.isDirty()) {
				batch.add(new UpdateEntity((EntityDTO) model, this
						.getChangedProperties(record)));
			}
		}

		for (ModelData child : treeStore.getChildren(model)) {
			prepareBatch(batch, child);
		}
	}

	public void onSelectionChanged(ModelData selectedItem) {
		view.setActionEnabled(UIActions.delete, this.db.isDesignAllowed()
				&& selectedItem instanceof EntityDTO);
	}

	public Object getWidget() {
		return view;
	}

	@Override
	protected void onSaved() {
		
	}

	@Override
	public PageId getPageId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean navigate(PageState place) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}
}