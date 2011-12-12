package org.sigmah.client.page.report;

import org.sigmah.client.inject.AppInjector;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageLoader;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.PageStateSerializer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class ReportDesignLoader implements PageLoader {

    private AppInjector injector;
    
    @Inject
    public ReportDesignLoader(AppInjector injector, NavigationHandler pageManager, PageStateSerializer placeSerializer){
    	this.injector = injector;

        pageManager.registerPageLoader(ReportDesignPresenter.PAGE_ID, this);
        placeSerializer.registerParser(ReportDesignPresenter.PAGE_ID, new ReportDesignPageState.Parser());
    }
	
	@Override
	public void load(PageId pageId, PageState pageState,
			final AsyncCallback<Page> callback) {
		 
		GWT.runAsync(new RunAsyncCallback() {
	            @Override
	            public void onFailure(Throwable throwable) {
	                callback.onFailure(throwable);
	            }

	            @Override
	            public void onSuccess() {
	                callback.onSuccess(injector.getReportDesignPresenter());
	            }
	        });
		
	}

}
