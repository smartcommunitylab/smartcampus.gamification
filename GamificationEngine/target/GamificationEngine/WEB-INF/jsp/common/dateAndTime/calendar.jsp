<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<%-- expected params: calendarName, cssDivDate (optional), cssDivCalendar (optional), 
     disableHour (optional), disableMinutes (optional) --%>
<%-- expected beans in model or in request scope: calendar, nowCalendar
     (ex. before include this jsp do something like <c:set var="calendar" value="${command.calendar}" scope="request"/>) --%>

<div class="${param.cssDivDate}">
    <label>
        <span><spring:message code="calendar.date.label"/></span>
    </label>
    <br/>
    <ct:springSelect path="${param.calendarName}.day"
                     titleInput="calendar.date.day.title"
                     items="${calendar.days}"
                     cssError="errorfield"
                     cssInput="left"
                     inputRequired="false"
                     showSelectLabel="false"
                     wrapped="false"
                     withLabel="false"
                     />
    <ct:springSelect path="${param.calendarName}.month"
                     titleInput="calendar.date.month.title"
                     items="${calendar.months}"
                     cssError="errorfield"
                     cssInput="left"
                     inputRequired="false"
                     showSelectLabel="false"
                     wrapped="false"
                     withLabel="false"
                     />
    <ct:springSelect path="${param.calendarName}.year"
                     titleInput="calendar.date.year.title"
                     items="${calendar.years}"
                     cssError="errorfield"
                     cssInput="left"
                     inputRequired="false"
                     showSelectLabel="false"
                     wrapped="false"
                     withLabel="false"
                     />
</div>

<c:if test="${empty param.disableHour}">
    <div class="thirdcolumn">
        <label>
            <span><spring:message code="calendar.hour.label"/></span>
        </label>
        <br/>
        <ct:springSelect path="${param.calendarName}.hour"
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
</c:if>

<c:if test="${empty param.disableMinutes}">
    <div class="thirdcolumn">
        <label>
            <span><spring:message code="calendar.minutes.label"/></span>
        </label>
        <br/>
        <ct:springSelect path="${param.calendarName}.minutes"
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

<div class="${cssDivCalendar}">
    <br/>
    <input class="hidden" type="text" id="nowCalendarDay" value="${nowCalendar.day}"/>
    <input class="hidden" type="text" id="nowCalendarMonth" value="${nowCalendar.month}"/>
    <input class="hidden" type="text" id="nowCalendarYear" value="${nowCalendar.year}"/>
    
    <input class="hidden" type="text" id="${param.calendarName}Datepicker"/>
</div>