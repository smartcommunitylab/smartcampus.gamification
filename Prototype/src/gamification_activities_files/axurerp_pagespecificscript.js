for(var i = 0; i < 15; i++) { var scriptId = 'u' + i; window[scriptId] = document.getElementById(scriptId); }

$axure.eventManager.pageLoad(
function (e) {

});
document.getElementById('u12_img').tabIndex = 0;

u12.style.cursor = 'pointer';
$axure.eventManager.click('u12', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Game_Design.html');

}
});
gv_vAlignTable['u8'] = 'center';document.getElementById('u10_img').tabIndex = 0;

u10.style.cursor = 'pointer';
$axure.eventManager.click('u10', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Game_Configuration.html');

}
});
gv_vAlignTable['u5'] = 'center';gv_vAlignTable['u1'] = 'center';gv_vAlignTable['u14'] = 'top';
$axure.eventManager.change('u6', function(e) {

if (true) {

	SetPanelState('u9', 'pd1u9','none','',500,'none','',500);

}
});
gv_vAlignTable['u2'] = 'top';gv_vAlignTable['u11'] = 'center';gv_vAlignTable['u13'] = 'center';