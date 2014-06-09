function toggleImage(positiveSrcImage, negativeSrcImage, flag, imageSelector) {
    var imageToSubstitute;
    
    if (flag == true) {
        imageToSubstitute = positiveSrcImage;
    } else if (flag == false) {
        imageToSubstitute = negativeSrcImage;
    }
    
    $(imageSelector).attr("src", imageToSubstitute);
}