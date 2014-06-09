$(document).ready(function(){			
    var parentElement = $('#parent_element').val();
    //how much items per page to show
    var show_per_page = parseInt($('#show_per_page').val());	
    //getting the amount of elements inside content div
    var number_of_items = $('#' + parentElement).children().size();  
    //calculate the number of pages we are going to have
    var number_of_pages = Math.ceil(number_of_items/show_per_page);  
	
    //numero di pagine selezionabili da mostrare
    var pages_per_web_page = parseInt($('#pages_per_web_page').val());
	
    if (number_of_pages > 1) {		
        //set the value of our hidden input fields
        $('#current_page').val(0);
		
        var navigation_html = buildPagination(number_of_items, show_per_page, number_of_pages, pages_per_web_page, 0, 0);
        $('#page_navigation').show();
        $('#page_navigation').html(navigation_html);  
		
        //add active_page class to the first page link
        $('#page_navigation .page_link:first').addClass('active_page').text('['+$('#page_navigation .page_link:first').text()+']');  
		
        //hide all the elements inside content div
        $('#' + parentElement).children().addClass('hidden');  
		
        //and show the first n (show_per_page) elements
        $('#' + parentElement).children().slice(0, show_per_page).removeClass('hidden');
    }
});

function buildPagination(number_of_items, show_per_page, number_of_pages, pages_per_web_page, idStartPage, idNewPage) {
    var navigation_html = "";
		
    if (idNewPage >= 0 && idNewPage < number_of_pages) {
		
        var idEndPage = buildEndPage(idStartPage, pages_per_web_page, number_of_pages);
		
        //se vanno aggiornate le pagine cliccabili mostrate (vale a dire,
        //avanzando o tornando indietro come pagina, non rimaniamo nel blocco attualmente mostrato
        //aggiorna gli indici
        if (idNewPage > idEndPage) {
            idStartPage += pages_per_web_page;
			
            idEndPage = buildEndPage(idStartPage, pages_per_web_page, number_of_pages);
        } else if (idNewPage < idStartPage) {
            idStartPage -= pages_per_web_page;
			
            idEndPage = buildEndPage(idStartPage, pages_per_web_page, number_of_pages);
        }
		
		  var separatore = '<li> - </li>';
        var indietroClass = 'class="previous_link';
        var avantiClass = 'class="next_link';
			
        //ultima pagina, nascondi avanti
        if (idNewPage == number_of_pages - 1) {
            avantiClass += ' hidden';
        } else if (idNewPage == 0) {//prima pagina, nascondi indietro			
            indietroClass += ' hidden';
        }
		
        navigation_html += '<div class="paginazione-div1"><ul id="paginationMenu">';
        navigation_html += '<li><a ' + indietroClass + '" href="javascript:previous();">« indietro</a></li>';  
		
        //elementi da mostrare
        for (var i = idStartPage; i <= idEndPage; i++) {			
            navigation_html += '<li><a class="page_link" href="javascript:go_to_page(' + i +')" longdesc="' + i +'" title="Vai alla pagina numero ' + i + '">'+ (i + 1) +'</a></li>'+separatore;
        }
		
        navigation_html += '<li><a ' + avantiClass + '" href="javascript:next();">avanti »</a></li>';  
        navigation_html += '</ul></div>';
        navigation_html += '<div class="paginazione-div2"><span class="title4">' + ((idNewPage * show_per_page) + 1) + '</span>-<span class="title4">' + buildEndEntry(idStartPage, pages_per_web_page, number_of_items, idNewPage, show_per_page) + '</span> di <span class="title4">' + number_of_items + ' soluzioni</span></div>'
    }
	
    return navigation_html;
}

function buildEndPage(idStartPage, pages_per_web_page, number_of_pages) {
    var idEndPage = -1;
	
    //se l'assegnazione dell'endPage non sforerebbe il totale delle pagine
    if (idStartPage + pages_per_web_page - 1 < number_of_pages) {
        idEndPage = idStartPage + pages_per_web_page - 1;
    } else {//se l'assegnazione dell'endPage sforerebbe il totale delle pagine
        idEndPage = number_of_pages - 1;
    }
	
    return idEndPage;
}

function buildEndEntry(idStartPage, pages_per_web_page, number_of_items, idNewPage, show_per_page) {
    var idEndEntry = -1;
	
    if (((idNewPage * show_per_page) + show_per_page) <= number_of_items) {
        idEndEntry = (idNewPage * show_per_page) + show_per_page;
    } else {
        idEndEntry = number_of_items;
    }
	
    return idEndEntry;
}

function previous() {
    var new_page = parseInt($('#current_page').val()) - 1;	
	
    go_to_page(new_page);	
}  

function next(){
    var currentPage = parseInt($('#current_page').val())
    var new_page = currentPage + 1;
	  
    go_to_page(new_page);	
}

function go_to_page(page_num) {
    var parentElement = $('#parent_element').val();
    //how much items per page to show
    var show_per_page = parseInt($('#show_per_page').val());	
    //getting the amount of elements inside content div  
    var number_of_items = $('#' + parentElement).children().size();  
    //calculate the number of pages we are going to have  
    var number_of_pages = Math.ceil(number_of_items/show_per_page);
	
    //numero di pagine selezionabili da mostrare
    var pages_per_web_page = parseInt($('#pages_per_web_page').val());
	
    //get the element number where to start the slice from  
    start_from = page_num * show_per_page;  
	
    //get the element number where to end the slice  
    end_on = start_from + show_per_page;  
	
    //hide all children elements of content div, get specific items and show them  
    $('#' + parentElement).children().addClass('hidden').slice(start_from, end_on).removeClass('hidden');
	
    var navigation_html = buildPagination(number_of_items, show_per_page, number_of_pages, pages_per_web_page, parseInt($('#page_navigation .page_link:first').attr('longdesc')), page_num);	
    $('#page_navigation').html(navigation_html);
	
	
    /*get the page link that has longdesc attribute of the current page and add active_page class to it 
	and remove that class from previously active page link*/  
    $('.page_link[longdesc=' + page_num +']').text('['+$('.page_link[longdesc=' + page_num +']').text()+']').addClass('active_page').parent().siblings().children('.active_page').removeClass('active_page'); 
	
    //update the current page input field  
    $('#current_page').val(page_num);
	
	
//inizio operazioni nascondi-mostra avanti indietro
	
//ultima pagina, nascondi avanti
/*if (page_num == number_of_pages - 1) {
		$('.next_link').parent().addClass('hidden');
		$('.previous_link').parent().removeClass('hidden');
	} else if (page_num == 0) {//prima pagina, nascondi indietro
		$('.previous_link').parent().addClass('hidden');
		$('.next_link').parent().removeClass('hidden');
	} else {//pagina intermedia tra la prima e l'ultima, mostra sia indietro che avanti
		$('.previous_link').parent().removeClass('hidden');
		$('.next_link').parent().removeClass('hidden');
	}*/
}