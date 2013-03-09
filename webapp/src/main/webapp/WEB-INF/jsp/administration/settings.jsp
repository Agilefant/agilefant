<%@ include file="../inc/_taglibs.jsp"%>

<struct:htmlWrapper navi="settings">

<jsp:body>

<h2>System settings</h2>

<script type="text/javascript">
<!--
$(document).ready(function() {
  var dwc = $('#dailyWorkCheckbox');

  if (dwc.attr('checked')) {
    $('#thresholdDiv').show();
  }

  dwc.change(function() {
    $('#thresholdDiv').toggle();
  });
  
});
//-->
</script>

<div class="structure-main-block">
<div class="dynamictable ui-widget-content ui-corner-all">

  <div class="dynamictable-caption dynamictable-caption-block ui-widget-header ui-corner-all">
    System wide settings
  </div>

  <div style="margin: 1em 0.5em 0.5em;">
    
    <div class="warning-note"> 
      <strong>Note!</strong> Modifying these settings will affect all users.
    </div>
  
    <ww:form action="storeSettings.action" method="post">
  
    <h3>Additional views</h3>
  
    <table class="settings-table">
    <tr>
      <td><label for="dailyWorkEnabled">Enable Daily Work</label></td>
      <td><ww:checkbox id="dailyWorkCheckbox" name="dailyWorkEnabled" fieldValue="true" value="%{dailyWorkEnabled}"></ww:checkbox></td>
      <td><a href="#" class="quickHelpLink" onclick="HelpUtils.openHelpPopup(this,'Daily Work','static/html/help/dailyWorkPopup.html'); return false;">What is Daily Work?</a></td>
    </tr>
    <tr>
      <td><label for="devPortfolioEnabled">Enable Portfolio</label></td>
      <td><ww:checkbox name="devPortfolioEnabled" fieldValue="true" value="%{devPortfolioEnabled}"></ww:checkbox></td>
      <td><a href="#" class="quickHelpLink" onclick="HelpUtils.openHelpPopup(this,'Portfolio','static/html/help/devPortfolioPopup.html'); return false;">What is Portfolio?</a></td>
    </tr>
    <tr>
      <td><label for="hourReportingEnabled">Enable Timesheets</label></td>
      <td><ww:checkbox  name="hourReportingEnabled" fieldValue="true" value="%{hourReportingEnabled}"></ww:checkbox></td>
      <td><a href="#" class="quickHelpLink" onclick="HelpUtils.openHelpPopup(this,'Timesheets','static/html/help/timesheetsPopup.html'); return false;">What are Timesheets?</a></td>
    </tr>
    </table>
    
    <ww:submit value="Save all" cssClass="dynamics-button"></ww:submit>
    

    
    <h3>Story tree</h3>

    <style>
    ul.storyTreeOrderList {
      display: block;
      margin: 0;
      padding: 0;
      width: 100%;
      height: 100%;
      white-space: nowrap;
    }
    ul.storyTreeOrderList li {
      display: inline-block;
      background: white;
      border: 1px solid #ccc;
      margin: 0.3em 0.5em;
      padding: 0.2em 0.4em;
      text-align: center;
      cursor: move;
      line-height: 1.5em;
      min-height: 1em;
      vertical-align: middle;
      -webkit-border-radius: 5px;
      -moz-border-radius: 5px;
      border-radius: 5px;
    }
    ul.storyTreeOrderList li span {
      vertical-align: middle;    
    }
    
    .backlogDraggable {
      font-size: 80%;
      color: #666;
    }
    
    </style>
    
    <script type="text/javascript">
    $(document).ready(function() {
      var orderInput = $('#storyTreeFieldOrder');
      $('#storyTreeIncludeThese').sortable({
        connectWith: '#storyTreeExcludeThese',
        tolerance: 'pointer',
        update: function() {
          
          var included = [];
          $.each($('#storyTreeIncludeThese > li'), function(k,v) {
            included.push($(v).attr('id'));
          });
          orderInput.val(included.join(','));
        },
        remove: function(event, ui) {
          if (ui.item.is('#name')) {
            MessageDisplay.Warning("Can't remove the name field");
            $(this).sortable('cancel');
          }
        }
      });
      $('#storyTreeExcludeThese').sortable({
        tolerance: 'pointer',
        connectWith: '#storyTreeIncludeThese'
      });
    });
    </script>

    <table class="settings-table">    
    <tr>
      <td title="These items will be shown in the story tree">Order of story info</td>
      <td colspan="2" style="height: 3em; min-width: 500px;">
        <input id="storyTreeFieldOrder" type="hidden" name="storyTreeFieldOrder" value="${settings.storyTreeFieldOrder}"/>
        <ul class="storyTreeOrderList" id="storyTreeIncludeThese">
          <c:forEach items="${settings.storyTreeFieldOrder}" var="fieldType">
            <aef:settingStoryTreeField fieldType="${fieldType}"/>
          </c:forEach>
        </ul>
      </td>
    </tr>
    <tr>
      <td title="These items will not be shown in the story tree">Not in story tree</td>
      <td colspan="2" style="height: 3em;">
        <ul class="storyTreeOrderList" id="storyTreeExcludeThese">
          <c:forEach items="state,storyPoints,labels,name,backlog,breadcrumb" var="fieldType">
            <c:if test="${!fn:contains(settings.storyTreeFieldOrder, fieldType)}">
              <aef:settingStoryTreeField fieldType="${fieldType}"/>
            </c:if>
          </c:forEach>
        </ul>
      </td>
    </tr>
    <tr>
      <td title="Affects how the branch metrics are calculated">Branch metrics type</td>
      <td>
        <ww:radio list="#{'off':'Off', 'leaf':'Leaf progress', 'estimate':'Branch progress', 'both':'Both'}" name="branchMetricsType"/>
      </td>
      <td>
        <a href="#" class="quickHelpLink" onclick="HelpUtils.openHelpPopup(this,'Branch metrics','static/html/help/branchMetricsPopup.html'); return false;">What are branch metrics?</a>
      </td>
    </tr>
    </table>
    
    
    <ww:submit value="Save all" cssClass="dynamics-button"></ww:submit>
    
    <h3>Backlogs</h3>
    
    <table class="settings-table">
    <tr>
      <td><label for="labelsInStoryList">Display labels in story lists</label></td>
      <td colspan="2"><ww:checkbox name="labelsInStoryList" fieldValue="true" value="%{labelsInStoryList}"></ww:checkbox></td>
    </tr>
    <tr>
      <td><label for="weekendsInBurndown">Display reference velocity in Burndown</label></td>
      <td colspan="2">
        <ww:radio list="#{'false':'as straight line', 'true':'take weekends to account'}" name="weekendsInBurndown" value="%{weekendsInBurndown}"/>
      </td>
    </tr>
    
    </table>
    
    <ww:submit value="Save all" cssClass="dynamics-button"></ww:submit>
     <!-- 
    	Configuration for file attachment in comments
    	
     -->
     
     <H3> File Attachment</H3>
     
     
     <table class="settings-table">
     	<tr>
     		<td><ww:text name="Location for saving files"></ww:text></td>
     		<td><ww:textfield  name="locationForSavingAttachment" id="fileSaveLocation" size="35" ></ww:textfield></td>
     		<c:if test="hasActionErrors()">
   				<div>
			      <ww:actionerror/>
			   </div>
			</c:if>
     	</tr>
     	<tr>
     		<td><ww:text name="Max.size of all files (MB)"></ww:text></td>
     		<td><ww:textfield  name="maxSizeOfAllAttachments" id="maxSizeOfAllAttachment" size="4"></ww:textfield></td>
     	</tr>  
     	<tr>
     		<td><ww:text name="Max.number of attachments per comment"></ww:text></td>
     		<td><ww:textfield  name="maxNumberOfAttachment" size="4" id="maxNumberOfAttachments" ></ww:textfield></td>
     	</tr>     
     </table>
     <ww:submit value="Save All" cssClass="dynamics-button"></ww:submit>
     <!-- 
     	End of configuration
      -->
    
    
    <div style="margin: 0; padding: 0; display: none;" id="thresholdDiv">
    
    <h3>Load thresholds</h3>
    
    <p>Load thresholds are used in displaying the workload in the Daily Work view. To restore default threshold, just leave the field empty.</p>
    
    <table class="settings-table">

    <tr>
      <td><ww:text name="load.threshold.maximum" /></td>
      <td><ww:textfield name="rangeHigh" id="maximumField" size="4" /> %</td>
      <td style="background: rgba(150, 8, 8, 0.7);">&nbsp;</td>
    </tr>
    <tr>
      <td><ww:text name="load.threshold.critical" /></td>
      <td><ww:textfield name="criticalLow" id="criticalLowField" size="4" /> %</td>
      <td style="background: rgba(224, 17, 2, 0.7);">&nbsp;</td>
    </tr>
    <tr>
      <td><ww:text name="load.threshold.optimalHigh" /></td>
      <td><ww:textfield name="optimalHigh" id="optimalHighField" size="4" /> %</td>
      <td style="background: rgba(245, 221, 57, 0.7);">&nbsp;</td>
    </tr>
    <tr>
      <td><ww:text name="load.threshold.optimalLow" /></td>
      <td><ww:textfield name="optimalLow" id="optimalLowField" size="4" /> %</td>
      <td style="background: rgba(9, 144, 14, 0.7)">&nbsp;</td>
    </tr>
    <tr>
      <td><ww:text name="load.threshold.low" /></td>
      <td><ww:textfield name="rangeLow" id="minimumField" size="4" /> %</td>
      <td style="background: rgba(130, 180, 244, 0.7);">&nbsp;</td>
    </tr>
    </table>
    
    <ww:submit value="Save all" cssClass="dynamics-button"></ww:submit>
    
    </div>
    </ww:form>
  </div>

</div>
</div>

</jsp:body>
</struct:htmlWrapper>