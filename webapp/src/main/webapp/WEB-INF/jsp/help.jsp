<%@ include file="./inc/_taglibs.jsp"%>

<struct:htmlWrapper navi="none">

<h2>Agilefant Quick Start Guide</h2>

<style type="text/css">
p.infoBox {
  background-color: #ffc;
  width: 50%;
  
  padding: 0.5em;
  margin: 1em;

  border: 3px solid rgba(255, 100, 0, 0.5);
  -moz-border-radius: 5px;
  -webkit-border-radius: 5px;
  border-radius: 5px;
}

div.rightHandImage {
  float: right;
  margin: 1em;
}

div.rightHandImage img {
  border: 1px solid #ccc;
  padding: 1em;
  
  -moz-border-radius: 5px;
  -webkit-border-radius: 5px;
  border-radius: 5px;
}

div.rightHandImage p {
  text-align: center;
  font-style: italic;
}
</style>

<p>
For more detail, check out the <a href="http://agilefant.org/support/user-guide/">user guide</a>. The <a href="FAQ http://agilefant.org/support/faq/">FAQ</a> - which contains even more detail regarding specific features.
<p>

</p>

<h4>Table of Contents</h4>
<ol>
  <li><a href="#exampleData">About the example data</a></li>
  <li><a href="#changingPassword">Changing Your Password</a></li>
  <li><a href="#backlogStructure">Products, Projects and Iterations</a></li>
  <li><a href="#storiesAndTasks">Stories and Tasks</a></li>
  <li><a href="#creatingUsers">Users and Access Rights</a></li>
  <li><a href="#additionalViews">Daily Work, Timesheets and the Portfolio View</a></li>
</ol>


<div class="structure-main-block">

<div class="dynamictable ui-widget-content ui-corner-all" id="exampleData">

<div class="ui-widget-header dynamictable-caption dynamictable-caption-block ui-corner-all">
1. About the example data 
</div>
<p>
If you signed up for a trial account, or imported the example database when you did the installation, your Agilefant is populated with example data: two products, a project with some iterations and a standalone iteration. You can see these in the 'All backlogs' section of the left hand side accordion.
<p>
Click on the plus (+) of the the Product named 'Agilefant'. You can see that it contains several projects. Click on the plus (+) sign of the project named '2013 Q1-Q3 TUTL'. There you can see that it contains several iterations. These contain stories and tasks. The Agilefant product is an example of how to use Agilefant for both long and short term planning. 
<p>There's also a Product named 'ToDo-list', which represents probably the simplest way to use Agilefant. It is a single project containing several stories (such as "Get christmas presents for everyone").
<p>In addition, there's a Standalone iteration (called 'Standalone iteration'), which represents a simple way to use Agilefant just for managing the iterations of a single team - for more info on standalone iterations, check out the <a href="http://agilefant.org/support/user-guide/">user guide</a>.

</div>

<div class="dynamictable ui-widget-content ui-corner-all" id="changingPassword">

<div class="ui-widget-header dynamictable-caption dynamictable-caption-block ui-corner-all">
2. Changing Your Password
</div>

<div class="rightHandImage">
  <img src="static/img/help/change_password.png" style=""/>
  <!-- <br/>
  <p style="text-align: center; font-style: italic;">The change password button</p>-->
</div>
<p>
Start by changing your password:

<ol>
  <li>Click your user's name in the top right corner of the page beside the 'Create new' text. This will take you to your account page.</li>
  <li>Click the button titled &quot;Change password&quot; in the User info title bar.</li>
</ol>

</div>
</div>





<div class="structure-main-block">
<div class="dynamictable ui-widget-content ui-corner-all" id="backlogStructure">

<div class="ui-widget-header dynamictable-caption dynamictable-caption-block ui-corner-all">
3. Products, Projects and Iterations 
</div>

<div class="rightHandImage">
  <img src="static/img/help/create_product.png" alt="Create new menu" />
  <br/>
  <p>The create new menu</p>
</div>

<h3>Backlogs in Agilefant</h3>

<p>Agilefant has three different levels of backlogs: Products, Projects
and Iterations. These represent different levels of planning.</p>

<p>The simplest way to use Agilefant is to create standalone iterations, and use them for iteration management. 

However, if you have more than one team to manage, or wish to leverage Agilefant's capabilities for product and/or portfolio management, it's a good idea to create Products, then Projects under these, and divide the Projects into Iterations.
 
<div style="width:40%; border: 1px solid #ccc; margin: 1em; -moz-border-radius: 5px; -webkit-border-radius: 5px; border-radius: 5px;">
<h4>An example of Agilefant's backlog structure</h4>

<ul style="list-style-type: none; list-style-image: url('static/img/hierarchy_arrow.png')">
  <li style="margin-left: 0em;">Example Product</li>
    <li style="margin-left: 2em;">Project #1</li>
      <li style="margin-left: 4em;">Iteration in progress</li>
      <li style="margin-left: 4em;">Upcoming iteration</li>
    <li style="margin-left: 2em;">Project #2</li>
      <li style="margin-left: 4em;">Past iteration</li>
      <li style="margin-left: 4em;">Iteration in progress</li>
  <li style="margin-left: 0em;">Another Product</li>
  <li style="margin-left: 0em;">Standalone iteration</li>
</ul>
</div>
<p>Unlike regular iterations, standalone iterations can include stories from multiple products. So, if you have a single team working on multiple Products and/or Projects, it's a good idea to gather all of the team's work into a Standalone iteration. Standalone iterations are 
created like regular iterations, with the exception that the 'parent project' in the creation dialog is simply left empty. 
For further info regarding standalone iterations, check out the related section in the <a href="http://agilefant.org/faq/how-do-standalone-iterations-work/">FAQ</a>.</p>

<h3>Creating a backlog</h3>

<ol>
  <li>Open the create new menu from the top right corner</li>
  <li>Click the 'Product &raquo;' link</li>
  <li>Enter the product's name and description and click ok</li>
</ol>

Do give yourself access when you create the Product! Otherwise, it won't appear on the left hand menu, unless you go to the Admin tab and give your team access to it.  After creating a Product, you can create Projects and Iterations, either from the 'Create new' menu, or going to the product itself.
<p>


</div>
</div>


<div class="structure-main-block">
<div class="dynamictable ui-widget-content ui-corner-all" id="storiesAndTasks">

<div class="ui-widget-header dynamictable-caption dynamictable-caption-block ui-corner-all">
4. Stories and Tasks
</div>

<div class="rightHandImage">
  <img src="static/img/help/story_info_bubble.png" alt="Story info bubble" />
  <br/>
  <p>By clicking on a story in the breakdown view, you can see and change its details</p>
</div>

<h3>Stories</h3>

A story is a piece of work that needs to be done. Stories can be created from each backlog, as well as from the Create new -> Story link.

<h3>Tasks</h3>

<p>
Tasks are the means of getting the stories done. They can reside
within a story, or directly in an iteration. While tasks can only be viewed
and edited in the iteration view, they do not disappear when a story is moved 
back to the project or product backlogs.
</p>

<h3>List and breakdown views</h3>

<p>
The work breakdown (or simply breakdown) is a view into the product and project backlogs that
displays how the smaller stories have been refined from the higher
level epics and features. 

<p>
Stories that have no children are called <em>leaf stories</em>. Iterations can contain only leaf stories; 
likewise the Backlog views into products and projects display only the leaf stories.
</p>

</div>
</div>

<div class="structure-main-block">
<div class="dynamictable ui-widget-content ui-corner-all" id="creatingUsers">

<div class="ui-widget-header dynamictable-caption dynamictable-caption-block ui-corner-all">
5. Users and Access Rights
</div>

<div class="rightHandImage">
  <img src="static/img/help/create_user.png" alt="Left hand menu"/>
  <br/>
  <p>Navigating with the left hand menu</p>
</div>

<p class="infoBox">
<strong>Note on user rights</strong><br/>
Agilefant makes a difference between Admin and Non-admin users, and supports limiting access to Products and/or Standalone iterations. 
You can also share Iterations as 'read-only'. For further information see the User Rights section in <a href="http://www.agilefant.org/support/faq/">FAQ</a>.
</p>

<ol>
  <li>Open the administration section from the left hand menu</li>
  <li>Click the link titled 'Users'</li>
  <li>Click the 'Create user' button in the Enabled users section's title.</li>
</ol>

</div>
</div>

<div class="structure-main-block">
<div class="dynamictable ui-widget-content ui-corner-all" id="additionalViews">

<div class="ui-widget-header dynamictable-caption dynamictable-caption-block ui-corner-all">
6. Daily Work, Timesheets and the Portfolio views
</div>

<%--
<div class="rightHandImage">
  <img src="static/img/help/additional_views.png" alt="Additional Views" />
  <br/>
  <p>Additional view settings</p>
</div>
 --%>

<p>
The Daily work, Timesheets and Portfolio views can be toggled on or off on the system settings page.</p>

<h3>Daily Work</h3>

<%@include file="/static/html/help/dailyWorkPopup.html" %>

<h3>Timesheets</h3>

<%@include file="/static/html/help/timesheetsPopup.html" %>

<h3>Portfolio</h3>

<%@include file="/static/html/help/devPortfolioPopup.html" %>

</div>
</div>



</struct:htmlWrapper>
