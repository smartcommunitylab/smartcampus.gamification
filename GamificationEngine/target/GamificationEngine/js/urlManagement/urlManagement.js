function makeLinksAbsolute(firstPart) {
    makeAbsolute('a[href^="/"]', 'href', firstPart);
}

function makeUrlImagesAbsolute(firstPart) {
    makeAbsolute('img[src^="/immagini/"]', 'src', firstPart);
}

function changeNthChildLinkUrl(linkSelector, nthChildIndex, newUrl) {
    $(linkSelector + ':nth-child(' + nthChildIndex + ')').attr('href', newUrl);
}

function makeAbsolute(selector, attrName, firstPart) {
    //Selects elements that have the specified attribute with a value beginning exactly with a given string
    $(selector).attr(attrName, function(index, attrValue) {
        return firstPart + attrValue;
    });
}