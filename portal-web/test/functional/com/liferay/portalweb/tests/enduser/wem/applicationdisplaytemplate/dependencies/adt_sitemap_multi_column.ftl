<#if entries?has_content>
	<@clay.row>
		<#list entries as entry>
			<@clay.col md="3">
				<div class="results-header">
					<h3>
						<a

						<#assign layoutType = entry.getLayoutType() />

						<#if layoutType.isBrowsable()>
							href="${portalUtil.getLayoutURL(entry, themeDisplay)}"
						</#if>

						>${entry.getName(locale)}</a>
					</h3>
				</div>

				<@displayPages
					depth=1
					pages=entry.getChildren()
				/>
			</@clay.col>
		</#list>
	</@clay.row>
</#if>

<#macro displayPages
	depth
	pages
>
	<#if pages?has_content && ((depth < displayDepth?number) || (displayDepth?number == 0))>
		<ul class="child-pages">
			<#list pages as page>
				<li>
					<a

					<#assign pageType = page.getLayoutType() />

					<#if pageType.isBrowsable()>
						href="${portalUtil.getLayoutURL(page, themeDisplay)}"
					</#if>

					>${page.getName(locale)}</a>

					<@displayPages
						depth=depth + 1
						pages=page.getChildren()
					/>
				</li>
			</#list>
		</ul>
	</#if>
</#macro>