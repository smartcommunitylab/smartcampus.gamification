for(var i = 0; i < 10; i++) { var scriptId = 'u' + i; window[scriptId] = document.getElementById(scriptId); }

$axure.eventManager.pageLoad(
function (e) {

});

$axure.eventManager.focus('u4', function(e) {

if ((GetWidgetText('u4')) == ('Search ConeTrees.com')) {

SetWidgetFormText('u4', '');

}
});

$axure.eventManager.blur('u4', function(e) {

if ((GetWidgetText('u4')) == ('')) {

SetWidgetFormText('u4', 'Search ConeTrees.com');

}
});
gv_vAlignTable['u5'] = 'top';
$axure.eventManager.focus('u6', function(e) {

if ((GetWidgetText('u6')) == ('Search ConeTrees.com')) {

SetWidgetFormText('u6', '');

}
});

$axure.eventManager.blur('u6', function(e) {

if ((GetWidgetText('u6')) == ('')) {

SetWidgetFormText('u6', 'Search ConeTrees.com');

}
});
gv_vAlignTable['u7'] = 'top';document.getElementById('u8_img').tabIndex = 0;

u8.style.cursor = 'pointer';
$axure.eventManager.click('u8', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Gamification_activities.html');

}
});
gv_vAlignTable['u9'] = 'center';gv_vAlignTable['u1'] = 'center';gv_vAlignTable['u3'] = 'center';