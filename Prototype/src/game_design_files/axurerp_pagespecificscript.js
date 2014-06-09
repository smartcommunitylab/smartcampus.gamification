for(var i = 0; i < 35; i++) { var scriptId = 'u' + i; window[scriptId] = document.getElementById(scriptId); }

$axure.eventManager.pageLoad(
function (e) {

});
gv_vAlignTable['u31'] = 'center';gv_vAlignTable['u16'] = 'top';document.getElementById('u28_img').tabIndex = 0;
HookHover('u28', false);

u28.style.cursor = 'pointer';
$axure.eventManager.click('u28', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Game_Configuration.html');

}
});
gv_vAlignTable['u29'] = 'center';gv_vAlignTable['u8'] = 'top';document.getElementById('u30_img').tabIndex = 0;
HookHover('u30', false);

u30.style.cursor = 'pointer';
$axure.eventManager.click('u30', function(e) {

if (true) {

    self.location.href="resources/reload.html#" + encodeURI($axure.globalVariableProvider.getLinkUrl($axure.pageData.url));

}
});
document.getElementById('u21_img').tabIndex = 0;

u21.style.cursor = 'pointer';
$axure.eventManager.click('u21', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Rules_Design.html');

}
});
gv_vAlignTable['u6'] = 'top';document.getElementById('u32_img').tabIndex = 0;

u32.style.cursor = 'pointer';
$axure.eventManager.click('u32', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Gamification_activities.html');

}
});

$axure.eventManager.mouseover('u32', function(e) {
if (!IsTrueMouseOver('u32',e)) return;
if (true) {

	SetPanelState('u27', 'pd1u27','none','',500,'none','',500);

}
});
gv_vAlignTable['u13'] = 'top';gv_vAlignTable['u15'] = 'center';gv_vAlignTable['u1'] = 'center';gv_vAlignTable['u26'] = 'center';gv_vAlignTable['u10'] = 'center';gv_vAlignTable['u11'] = 'top';gv_vAlignTable['u12'] = 'top';gv_vAlignTable['u7'] = 'top';gv_vAlignTable['u17'] = 'top';gv_vAlignTable['u23'] = 'top';gv_vAlignTable['u4'] = 'center';document.getElementById('u19_img').tabIndex = 0;

u19.style.cursor = 'pointer';
$axure.eventManager.click('u19', function(e) {

if (true) {

	self.location.href=$axure.globalVariableProvider.getLinkUrl('Actions_Design.html');

}
});
gv_vAlignTable['u20'] = 'center';gv_vAlignTable['u5'] = 'top';gv_vAlignTable['u22'] = 'center';
$axure.eventManager.mouseout('u25', function(e) {
if (!IsTrueMouseOut('u25',e)) return;
if (true) {

	SetPanelState('u27', 'pd0u27','none','',500,'none','',500);

}
});
gv_vAlignTable['u33'] = 'center';