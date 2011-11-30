/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import java.util.List;

import org.sigmah.shared.report.content.FilterDescription;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

/**
 * Support routines for ItextRenderers
 */
class ItextRendererHelper {

	private ItextRendererHelper() {}
	
    /**
     * Adds a set of paragraphs describing the filters which are applied to this
     * given ReportElement.
     * @param document
     * @param filterDescriptions
     * @throws DocumentException
     */
  public static void addFilterDescription(Document document, List<FilterDescription> filterDescriptions) throws DocumentException {
    for(FilterDescription description : filterDescriptions) {
      document.add(ThemeHelper.filterDescription(description.joinLabels(", ")));
    }
  }
}
