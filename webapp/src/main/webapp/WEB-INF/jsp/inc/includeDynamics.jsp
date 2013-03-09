<%@taglib uri="/WEB-INF/tlds/aef.tld" prefix="aef"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:choose>
	<c:when test="${aef:releaseMode()}">
		<aef:javascript path="dynamics" minify="always" />
	</c:when>
	<c:otherwise>
		<aef:javascript path="dynamics/Dynamics.events" />

		<aef:javascript path="utils/ArrayUtils" />
		<aef:javascript path="utils/ClassUtils" />
		<aef:javascript path="utils/Parsers" />
		<aef:javascript path="utils/XworkSerializer" />

		<aef:javascript path="dynamics/view/ViewPart" />
		<aef:javascript path="dynamics/view/CommonSubView" />
		<aef:javascript path="dynamics/view/CommonFragmentSubView" />
		<aef:javascript path="dynamics/view/MessageDisplay" />
		<aef:javascript path="dynamics/view/DynamicView" />
		<aef:javascript path="dynamics/view/Table" />
		<aef:javascript path="dynamics/view/Row" />
		<aef:javascript path="dynamics/view/Cell" />
		<aef:javascript path="dynamics/view/subviews/RowActions" />
		<aef:javascript path="dynamics/view/TableConfiguration" />
		<aef:javascript path="dynamics/view/subviews/Toggle" />
		<aef:javascript path="dynamics/view/TableCaption" />
		<aef:javascript path="dynamics/view/TableCellEditors" />
		<aef:javascript path="dynamics/view/decorators" />
		<aef:javascript path="dynamics/view/subviews/SplitPanel" />
		<aef:javascript path="dynamics/view/subviews/Tabs" />
		<aef:javascript path="dynamics/view/subviews/Buttons" />
		<aef:javascript path="dynamics/view/subviews/Button" />
		<aef:javascript path="dynamics/view/ValidationManager" />
		<aef:javascript path="dynamics/view/ConfirmationDialog" />
		<aef:javascript path="dynamics/view/ChangePasswordDialog" />
		<aef:javascript path="dynamics/view/LazyLoadedDialog" />
		<aef:javascript path="dynamics/view/UserSpentEffortWidget" />
		<aef:javascript path="dynamics/view/subviews/LabelsView" />
		<aef:javascript path="dynamics/view/subviews/LabelsIcon" />
		<aef:javascript path="dynamics/view/subviews/AutoSuggest" />
		<aef:javascript path="dynamics/view/SearchByTextWidget" />
		<aef:javascript path="dynamics/view/subviews/StoryInfoWidget" />
		<aef:javascript path="dynamics/view/StoryFiltersView" />
		<aef:javascript path="dynamics/view/StateFilterWidget" />
		<aef:javascript path="dynamics/view/SpentEffortWidget" />
		<aef:javascript path="dynamics/view/Bubble" />
		<aef:javascript path="dynamics/view/subviews/CellBubble" />
		<aef:javascript path="dynamics/view/MultiEditWidget" />		
		<aef:javascript path="dynamics/view/UserCommentWidget" />
		<aef:javascript path="dynamics/view/CommentWidget" />
		
		<aef:javascript path="dynamics/model/CommonModel" />
		<aef:javascript path="dynamics/model/BacklogModel" />
		<aef:javascript path="dynamics/model/IterationModel" />
		<aef:javascript path="dynamics/model/ProjectModel" />
		<aef:javascript path="dynamics/model/ProductModel" />
		<aef:javascript path="dynamics/model/StoryModel" />
		<aef:javascript path="dynamics/model/TaskModel" />
		<aef:javascript path="dynamics/model/UserModel" />
		<aef:javascript path="dynamics/model/TeamModel" />
		<aef:javascript path="dynamics/model/comparators" />
		<aef:javascript path="dynamics/model/AssignmentModel" />
		<aef:javascript path="dynamics/model/HourEntryModel" />
		<aef:javascript path="dynamics/model/TaskSplitContainer" />
		<aef:javascript path="dynamics/model/UserListContainer" />
		<aef:javascript path="dynamics/model/TeamListContainer" />
		<aef:javascript path="dynamics/model/HourEntryListContainer" />
		<aef:javascript path="dynamics/model/PortfolioModel" />
		<aef:javascript path="dynamics/model/LabelModel" />
		<aef:javascript path="dynamics/model/DailyWorkModel" />
		<aef:javascript path="dynamics/model/WorkQueueTaskModel" />
		<aef:javascript path="dynamics/model/ModelFactory" />
		<aef:javascript path="dynamics/model/AccessListContainer" />

		<aef:javascript path="dynamics/controller/CommonController" />
		<aef:javascript path="dynamics/controller/BacklogController" />
		<aef:javascript path="dynamics/controller/TaskController" />
		<aef:javascript path="dynamics/controller/AssignmentController" />
		<aef:javascript path="dynamics/controller/StoryController" />
		<aef:javascript path="dynamics/controller/IterationController" />
		<aef:javascript path="dynamics/controller/StoryListController" />
		<aef:javascript path="dynamics/controller/ProductController" />
		<aef:javascript path="dynamics/controller/IterationRowController" />
		<aef:javascript path="dynamics/controller/ProjectController" />
		<aef:javascript path="dynamics/controller/ProjectRowController" />
		<aef:javascript path="dynamics/controller/DailyWorkController" />
		<aef:javascript path="dynamics/controller/TasksWithoutStoryController" />
		<aef:javascript path="dynamics/controller/DailyWorkStoryListController" />
		<aef:javascript path="dynamics/controller/DailyWorkTasksWithoutStoryController" />
		<aef:javascript path="dynamics/controller/WorkQueueController" />
		<aef:javascript path="dynamics/controller/TaskSplitDialog" />
		<aef:javascript path="dynamics/controller/UserController" />
		<aef:javascript path="dynamics/controller/StoryInfoBubble" />
		<aef:javascript path="dynamics/controller/UserListController" />
		<aef:javascript path="dynamics/controller/UserRowController" />
		<aef:javascript path="dynamics/controller/TeamListController" />
		<aef:javascript path="dynamics/controller/TeamRowController" />
		<aef:javascript path="dynamics/controller/CreateDialog" />
		<aef:javascript path="dynamics/controller/TaskInfoDialog" />
		<aef:javascript path="dynamics/controller/HourEntryController" />
		<aef:javascript path="dynamics/controller/HourEntryListController" />
		<aef:javascript path="dynamics/controller/StoryTreeController" />
		<aef:javascript path="dynamics/controller/PortfolioController" />
		<aef:javascript path="dynamics/controller/PortfolioRowController" />
		<aef:javascript path="dynamics/controller/MyAssignmentsMenuController" />
		<aef:javascript	path="dynamics/controller/AdministrationMenuController" />
		<aef:javascript path="dynamics/controller/PersonalLoadController" />
		<aef:javascript path="dynamics/controller/AccessListController" />
		<aef:javascript path="dynamics/controller/AccessRowController" />
		<aef:javascript path="dynamics/controller/ROIterationController" />
		<aef:javascript path="dynamics/controller/ROStoryListController" />		
		<aef:javascript path="dynamics/controller/CommentController" />
		
		<aef:javascript path="autocomplete/autocompleteSearchBox" />
		<aef:javascript path="autocomplete/autocompleteSelectedBox" />
		<aef:javascript path="autocomplete/autocompleteRecent" />
		<aef:javascript path="autocomplete/autocompleteBundle" />
		<aef:javascript path="autocomplete/autocompleteDataProvider" />
		<aef:javascript path="autocomplete/autocompleteDialog" />
		<aef:javascript path="autocomplete/autocompleteSingleDialog" />
		<aef:javascript path="autocomplete/autocompleteInline" />
		
	</c:otherwise>
</c:choose>