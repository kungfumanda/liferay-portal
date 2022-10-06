<@liferay_ui["panel-container"]
	extended=true
	id="${namespace + 'facetTagPanelContainer'}"
	markupView="lexicon"
	persistState=true
>
	<@liferay_ui.panel
		collapsible=true
		cssClass="search-facet"
		id="${namespace + 'facetTagPanel'}"
		markupView="lexicon"
		persistState=true
		title="tag"
	>
		<ul class="list-unstyled tag-cloud">
			<#if entries?has_content>
				<#list entries as entry>
					<span class="facet-value">
						<button
							class="btn btn-link btn-unstyled facet-term ${(entry.isSelected())?then('facet-term-selected', 'facet-term-unselected')} tag-popularity-${entry.getPopularity()} term-name"
							data-term-id="${entry.getBucketText()}"
							disabled
							onClick="Liferay.Search.FacetUtil.changeSelection(event);"
						>
							${htmlUtil.escape(entry.getBucketText())}
						</button>
					</span>
				</#list>
			</#if>
		</ul>

		<#if !tagSearchFacetDisplayContext.isNothingSelected()>
			<@liferay_aui.button
				cssClass="btn-link btn-unstyled facet-clear-btn"
				onClick="Liferay.Search.FacetUtil.clearSelections(event);"
				value="clear"
			/>
		</#if>
	</@>
</@>