/**
 * Created by jiyuze on 2017/8/2.
 */
var addTimesModal = $('#addTimesModal');
var textarea = $("textarea[name='context']");
addTimesModal.on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);// Button that triggered the modal
    var recipient = button.data('show'); // Extract info from data-* attributes
    var modal = $(this);
    modal.find('.modal-title').text(recipient);
});
function upTimes(id) {
    var name = addTimesModal.find('.modal-title').html();
    var key = true;
    var text = textarea.val();
    textarea.val('');
    if(name === '减少次数'){
        key = false;
    }
    $.post("changeTimes",{
        bugId: id,
        flag: key,
        context: text,
        _csrf:_csrf.token
    },function (data) {
        $("#bugTimes").val(data);
    });
}
function deleteFile(docId){
    $("#del_a_" + docId).html("删除中...");
    $.post("../deleteFile",{_csrf:_csrf.token,docId:docId},function(data){
        window.location.reload(true);
    });
}
$(document).ready(function () {
    $("#uploadFile").fileinput({
        dropZoneEnabled: false,
        uploadUrl: '../uploadFile',
        language : 'zh',
        maxFileSize : 50000,
        uploadExtraData: function () {
            var obj = {};
            obj.bugId = $("input[name='bugId']").val();
            eval('obj.' + _csrf.parameterName + '= "' + _csrf.token + '"');
            return obj;
        }
    });
});