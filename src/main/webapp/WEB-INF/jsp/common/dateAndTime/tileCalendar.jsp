<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<%-- expected elements: calendar, calendarName, cssDivDate (optional), 
                        cssDivCalendar (optional), 
     disableHour (optional), disableMinutes (optional) --%>
<%-- expected beans in model or in request scope: nowCalendar --%>

<tiles:useAttribute id="calendar" name="calendar" ignore="false" />
<tiles:useAttribute id="calendarName" name="calendarName" classname="java.lang.String" ignore="false" />

<tiles:useAttribute id="cssMainDivDate" name="cssMainDivDate" classname="java.lang.String" ignore="true" />
<tiles:useAttribute id="cssDivDate" name="cssDivDate" classname="java.lang.String" ignore="true" />
<tiles:useAttribute id="cssDivCalendar" name="cssDivCalendar" classname="java.lang.String" ignore="true" />

<tiles:useAttribute id="disableHour" name="disableHour" classname="java.lang.String" ignore="true" />
<tiles:useAttribute id="disableMinutes" name="disableMinutes" classname="java.lang.String" ignore="true" />

<div class="${cssMainDivDate}">
    <div class="${cssDivDate}">
        <label>
            <span><spring:message code="calendar.date.label"/></span>
        </label>
        <br/>
        <ct:springSelect path="${calendarName}.day"
                         titleInput="calendar.date.day.title"
                         items="${calendar.days}"
                         cssError="errorfield"
                         inputRequired="false"
                         showSelectLabel="false"
                         wrapped="false"
                         withLabel="false"
                         />
        <ct:springSelect path="${calendarName}.month"
                         titleInput="calendar.date.month.title"
                         items="${calendar.months}"
                         cssError="errorfield"
                         inputRequired="false"
                         showSelectLabel="false"
                         wrapped="false"
                         withLabel="false"
                         />
        <ct:springSelect path="${calendarName}.year"
                         titleInput="calendar.date.year.title"
                         items="${calendar.years}"
                         cssError="errorfield"
                         inputRequired="false"
                         showSelectLabel="false"
                         wrapped="false"
                         withLabel="false"
                         />
        <input class="hidden" type="text" id="nowCalendarDay" value="${nowCalendar.day}"/>
        <input class="hidden" type="text" id="nowCalendarMonth" value="${nowCalendar.month}"/>
        <input class="hidden" type="text" id="nowCalendarYear" value="${nowCalendar.year}"/>
        <input class="hidden" type="text" id="${calendarName}Datepicker"/>
    </div>

    <c:if test="${empty disableHour}">
        <div class="left l-space">
            <label>
                <span><spring:message code="calendar.hour.label"/></span>
            </label>
            <br/>
            <ct:springSelect path="${calendarName}.hour"
                             titleInput="calendar.hour.title"
                             items="${calendar.hours}"
                             itemSelected="${calendar.hour}"
                             cssError="errorfield"
                             inputRequired="false"
                             showSelectLabel="false"
                             wrapped="false"
                             withLabel="false"
                             />
        </div>            
        <div class="left margin-l">
            <label>
                <span><spring:message code="calendar.minutes.label"/></span>
            </label>
            <br/>
            <ct:springSelect path="${calendarName}.minutes"
                             titleInput="calendar.minutes.title"
                             items="${calendar.minutesPossible}"
                             itemSelected="${calendar.minutes}"
                             cssError="errorfield"
                             inputRequired="false"
                             showSelectLabel="false"
                             wrapped="false"
                             withLabel="false"
                             />
        </div>
    </c:if>

</div>