var calendarDateSeparator = '-';

var lastPartCalendarDay = "\\.day";
var lastPartCalendarMonth = "\\.month";
var lastPartCalendarYear = "\\.year";

function setCalendar(datePickerSelector, calendarSelector) {
    
    var daySelector = calendarSelector + lastPartCalendarDay;
    var monthSelector = calendarSelector + lastPartCalendarMonth;
    var yearSelector = calendarSelector + lastPartCalendarYear;
    
    //var urlImage = webApp_absolute + "/img/8.gif";
    
    var dateFormat = 'd-m-yy';
    
    var minDate = getNowDate();
    
    var maxDate = '31-12-' + $(yearSelector + ' option:last-child').val();
    
    $(datePickerSelector).datepicker({
        showOn: "button",
        buttonImage: urlImage,
        buttonImageOnly: true,
        dateFormat: dateFormat,
        minDate: minDate, 
        maxDate: maxDate, 
        altField: 'icona calendario',
        buttonText: 'scegliere la data',
        dayNamesMin: ["Do", "Lu", "Ma", "Me", "Gi", "Ve", "Sa"],
        monthNames: ["Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno","Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"],
        onSelect: updateSelected
    });
    
    // Update three select controls to match a datepicker selection 
    function updateSelected() {
        var date = $(datePickerSelector).datepicker('getDate');
        $(daySelector).val(date ? date.getDate() : '');
        $(monthSelector).val(date ? date.getMonth() + 1 : '');            
        $(yearSelector).val(date ? date.getFullYear() : '');
    }
    
    // Update datepicker from three select controls
    $(daySelector + ', ' + monthSelector + ', ' + yearSelector).change(function() {
        $(datePickerSelector).datepicker('setDate', new Date(
            $(yearSelector).val(), $(monthSelector).val() - 1, $(daySelector).val()));
    });
    
    //Aggiornamento del numero dei giorni sulla base dei changes, a seconda del
    //mese ed anno selezionati
    /*
    // Update datepicker from three select controls
    $(daySelector).change({refresh: false}, updateDatepicker);
                                        
    // Update datepicker from three select controls
    $(monthSelector + ', ' + yearSelector).change({refresh: true, 
                                                   datePickerIdSelector: datePickerSelector}, 
                                                  updateDatepicker);
    
    // Update datepicker from three select controls
    function updateDatepicker(event) {
        if (event.data.refresh) {
            setDays(daySelector, event.data.datePickerIdSelector);
        }
        
        $(datePickerSelector).datepicker('setDate', new Date(
            $(yearSelector).val(), $(monthSelector).val() - 1, $(daySelector).val()));
    }
    
    function manageCityEvent(event) {
        var cityIdSelector = event.data.cityIdSelector;
        var provinceIdSelector = event.data.provinceIdSelector;

        setCities(cityIdSelector, provinceIdSelector);
    }

    function daysInMonth(month, year) {
        return new Date(year, month, 0).getDate();
    }
    
    function setDays(dayIdSelector, provinceIdSelector, valueSelected, 
                      countDays) {
        
        var date = $(datePickerIdSelector).datepicker('getDate');
        
        jQuery(cityIdSelector).empty();

        var daySelected = jQuery(dayIdSelector + ' option:selected').val();
        
        refreshDays(dayIdSelector, '', false, daySelected, );
    }

    function refreshDays(elementSelector, firstOption, skipFirstElement,
                          valueSelected, countDays) {
    
        jQuery(elementSelector).empty();

        var attributeOptionSelectedHtml = ' selected=selected';
        var html = firstOption;
        
        for (var i = 1; i <= countDays; i++) {
            if (skipFirstElement == false || index != '0') {
                var selected = '';
            
                if (valueSelected == i) {
                    selected = attributeOptionSelectedHtml;
                }
            
                html += '<option value="' + jQuery(value).attr(xmlAttrValueName) +
                '"' + selected + '>' + jQuery(value).text() + '</option>';
            }
        }
                     
        jQuery(elementSelector).append(html);
    }*/
};

function getNowDate() {
    var nowCalendarSelector = '#nowCalendar';
    
    var nowDate = buildDate(nowCalendarSelector + 'Day',
                            nowCalendarSelector + 'Month',
                            nowCalendarSelector + 'Year');
    
    return nowDate;
}

function buildDate(dateDaySelector, dateMonthSelector, dateYearSelector) {
    var dateToReturn = $(dateDaySelector).val() + calendarDateSeparator +
                       $(dateMonthSelector).val() + calendarDateSeparator +
                       $(dateYearSelector).val();
    
    return dateToReturn;
}

function checkSelectedDateWithNowDate(mainSelector, calendarSelector) {

        var nowDate = getNowDate();
        
        var daySelector = calendarSelector + lastPartCalendarDay;
        var monthSelector = calendarSelector + lastPartCalendarMonth;
        var yearSelector = calendarSelector + lastPartCalendarYear;
    
        var checkOk = false;
        try{
            day_select = $(jQuery(daySelector, mainSelector)).val();
            month_select = $(jQuery(monthSelector, mainSelector)).val();
            year_select = $(jQuery(yearSelector, mainSelector)).val();
            if(compareDates(nowDate, day_select, month_select, year_select)){
                checkOk = true;
            }
            else{
                alert('Assicurarsi che la data non sia precedente alla data odierna');
                checkOk = false;
            }
        }catch(e){}

        return checkOk;
}

function checkChronologicallyCalendarDates(mainSelector, firstCalendarSelector, 
                                           secondCalendarSelector) {
        
        var firstDaySelector = firstCalendarSelector + lastPartCalendarDay;
        var firstMonthSelector = firstCalendarSelector + lastPartCalendarMonth;
        var firstYearSelector = firstCalendarSelector + lastPartCalendarYear;
    
        var firstDate = buildDate(firstDaySelector, firstMonthSelector, 
                                  firstYearSelector);
        
        var secondDaySelector = secondCalendarSelector + lastPartCalendarDay;
        var secondMonthSelector = secondCalendarSelector + lastPartCalendarMonth;
        var secondYearSelector = secondCalendarSelector + lastPartCalendarYear;
        
        var checkOk = false;
        try{
            day_select = $(jQuery(secondDaySelector, mainSelector)).val();
            month_select = $(jQuery(secondMonthSelector, mainSelector)).val();
            year_select = $(jQuery(secondYearSelector, mainSelector)).val();
            if(compareDates(firstDate, day_select, month_select, year_select)){
                checkOk = true;
            }
            else{
                alert('Assicurarsi che la data di rientro non sia precedente a quella di partenza');
                checkOk = false;
            }
        }catch(e){}

        return checkOk;
}

function compareDates(date1, day2, month2, year2){
    dates1 = date1.split(calendarDateSeparator);
    var compareDate1 = new Date(dates1[2], dates1[1], dates1[0]);
    var compareDate2 = new Date(year2, month2, day2);
    if(compareDate2 < compareDate1){
        return false;
    }
    else {
        return true;
    }
}