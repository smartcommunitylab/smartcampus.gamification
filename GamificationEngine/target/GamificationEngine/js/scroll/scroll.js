function scrollTo(elementSelector, duration) {
    //necessario aggiungere il controllo per diverso da 0 perchè per jQuery 
    //undefined e 0 è uguale
    var isInIframe = (window.location != window.parent.location) ? true : false;
    
    if (duration != 0 &&
        (duration == undefined || duration == '')) {
        
        duration = 'slow';
    }
    if(!isInIframe){
      $('html,body').animate({ scrollTop: $(elementSelector).offset().top },
                           { duration: duration, easing: 'swing'});
    }
    else{
      parent.scrollTo('#'+window.frameElement.id, 500);      
    }
    
    return false;
}