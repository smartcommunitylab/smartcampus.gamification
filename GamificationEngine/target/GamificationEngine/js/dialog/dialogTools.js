var simplifiedViewParamWithValue = "&simplified=true";

function openPlaceSearchDialog(src, placeName) {
    src = src + simplifiedViewParamWithValue;
    
    var dialogContainerSelector = buildDialogContainerSelector("#" + placeName);
    var dialogId = buildDialogId(placeName);
    var dialogSelector = buildDialogSelector('#' + placeName);
    //$(dialogContainerSelector).append('<iframe id="' + dialogId + '" src="' + src + '" width="100%" height="100%" frameborder="0" scrolling="no" style="min-width: 95%;height:100%;">[Your browser does not support frames or is currently configured not to display frames. Please use an up-to-date browser that is capable of displaying frames.]</iframe>');
    //$(dialogSelector).load(src);
/*    $(dialogSelector).dialog({
        minWidth: 600,
        minHeight: 700,
        modal: true,
        autoOpen: true
    });*/
    //toggleImage(plusIcon, minusIcon, advancedOptionsHidden, '#advancedOptionsImage');   
    return false;
}

function closeParentPlaceSearchDialog(placeName) {
    window.parent.$(buildDialogSelector('#' + placeName)).dialog('close');
}

function manageSetPlaceDialog(placeName, x, y, latitude, longitude, 
                              description) {
                                  
    /*var placeLocalitaSelector = buildLocalitaSelector(prefixPlaceName);
      var placeLatitudeSelector = buildLatitudeSelector(prefixPlaceName);
      var placeLongitudeSelector = buildLongitudeSelector(prefixPlaceName);

      window.parent.$(placeLocalitaSelector).val('');
      window.parent.$(placeLatitudeSelector).val('');
      window.parent.$(placeLongitudeSelector).val('');

      window.parent.$(placeLocalitaSelector).val(description);
      window.parent.$(placeLatitudeSelector).val(firstCoordinate);
      window.parent.$(placeLongitudeSelector).val(secondCoordinate);*/
    
    closeParentPlaceSearchDialog(placeName);
    
    postParentAndSetPlace(placeName, description, x, y, latitude, longitude);
    
    return false;
}