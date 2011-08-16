package org.sigmah.client.page.search;

import java.util.List;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.monitor.NullAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.map.AIMapWidget;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.SearchHitDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.report.content.PivotContent;
import org.sigmah.shared.report.content.PivotTableData.Axis;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.TextBox;

public class SearchResultsPage extends ContentPanel implements SearchView {
	private VerticalPanel panelSearchResults;
	private VerticalPanel panelCompleteResult;
	private SearchResult searchResult;
	private PivotContent pivotContent;
	private SearchFilterView filterView;
	private RecentSitesView recentSitesView;

	private TextBox textboxSearch;
	private AsyncMonitor loadingMonitor = new NullAsyncMonitor();
	private SimpleEventBus eventBus = new SimpleEventBus();
	private AIMapWidget mapWidget;
	private String searchQuery;

	public SearchResultsPage() {
		initializeComponent();
	}

	private void initializeComponent() {
		setHeading("Search results");
		setLayout(new BorderLayout());

		SearchResources.INSTANCE.searchStyles().ensureInjected();

		createCompleteResultPanel();
		createFilterView();
		createSearchResultsPanel();
		createRecentSitesView();
		createSearchBox();
	}

	private void createRecentSitesView() {
		recentSitesView = new RecentSitesView();

		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.EAST);
		bld.setSplit(true);
		bld.setCollapsible(true);

		add(recentSitesView, bld);
	}

	private void createSearchResultsPanel() {
		panelSearchResults = new VerticalPanel();
		panelSearchResults.setScrollMode(Scroll.AUTO);
		panelCompleteResult.add(panelSearchResults);
	}

	private void createCompleteResultPanel() {
		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.CENTER);
		bld.setSplit(true);

		panelCompleteResult = new VerticalPanel();

		add(panelCompleteResult, bld);
	}

	private void createFilterView() {
		filterView = new SearchFilterView();
		panelCompleteResult.add(filterView);
	}

	private void createSearchBox() {
		textboxSearch = new TextBox();
		textboxSearch.setSize("2em", "2em");
		textboxSearch.setStylePrimaryName("searchBox");
		textboxSearch.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
						&& textboxSearch.getText().length() > 2) {
					eventBus.fireEvent(new SearchEvent(textboxSearch.getText()));
				}
			}
		});

		BorderLayoutData bld = new BorderLayoutData(LayoutRegion.NORTH);
		bld.setSize(50);
		bld.setSplit(true);

		add(textboxSearch, bld);
	}

	@Override
	public void setParent(SearchResult parent) {
		searchResult = parent;
	}

	@Override
	public void setItems(List<SearchHitDTO> items) {
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public AsyncMonitor getLoadingMonitor() {
		return loadingMonitor;
	}

	@Override
	public void setValue(SearchHitDTO value) {
	}

	@Override
	public SearchHitDTO getValue() {
		return null;
	}

	@Override
	public HandlerRegistration addSearchHandler(SearchHandler handler) {
		return eventBus.addHandler(SearchEvent.TYPE, handler);
	}

	@Override
	public void setSearchResults(PivotContent pivotTabelData) {
		this.pivotContent = pivotTabelData;

		showSearchResults();
	}

	private void showSearchResults() {
		panelSearchResults.removeAll();

		LabelField labelResults = new LabelField();
		labelResults.setText(I18N.MESSAGES
				.searchResultsFound("26", searchQuery));
		panelSearchResults.add(labelResults);
		VerticalPanel panelSpacer = new VerticalPanel();
		panelSpacer.setHeight(16);
		panelSearchResults.add(panelSpacer);

		for (Axis axis : pivotContent.getData().getRootRow().getChildren()) {
			SearchResultItem itemWidget = new SearchResultItem();
			itemWidget.setDabaseName(axis.getLabel());
			itemWidget.setChilds(axis.getChildList());

			panelSearchResults.add(itemWidget);
		}

		layout(true);
	}

	@Override
	public void setSearchQuery(String query) {
		this.searchQuery = query;

		textboxSearch.setText(query);
	}

	@Override
	public void setFilter(Filter filter) {
		filterView.setFilter(filter);
	}

	@Override
	public void setLatestAdditions(List<SiteDTO> latestAdditions) {
		recentSitesView.setSites(latestAdditions);
	}

}
