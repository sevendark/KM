/**
 * Created by jiyuze on 2017/8/2.
 */
$.fn.modal.Constructor.prototype.enforceFocus = function () {
    modal_this = this
    $(document).on('focusin.modal', function (e) {
        if (modal_this.$element[0] !== e.target &&
            !modal_this.$element.has(e.target).length &&
            !$(e.target.parentNode).hasClass('cke_dialog_ui_input_select') &&
            !$(e.target.parentNode).hasClass('cke_dialog_ui_input_text')) {
            modal_this.$element.focus();
        }
    })
};
var table_id_example = $('#table_id_example');
var dateTimeRange = $('#dateTimeRange');
function change(kindId) {
    $("input[name='bugKind']").val(kindId);
    table_id_example.DataTable().draw();
}
$(document).ready( function () {

    iniDateRangPicker(dateTimeRange);

    var table = table_id_example.DataTable( {
        language: dataTableTranslation,
        serverSide: true,
        processing: true,
        orderMulti: true,
        ajax: {
            url: 'bugData',
            data : function(data) {
                for (var i = 0; i < data.columns.length; i++) {
                    delete(data.columns[i].search);
                }
                data.startTime = dateTimeRange.data('daterangepicker').startDate.format('YYYY-MM-DD HH:mm')+ ':00';
                data.endTime = dateTimeRange.data('daterangepicker').endDate.format('YYYY-MM-DD HH:mm') + ':59';
                data.other = {kindId: ''};
                data.other.kindId = $("input[name='bugKind']").val();
            }
        },
        columns: [
            { data: 'bugId'},
            { data: 'bugName'},
            { data: 'bugTimes', searchable: false},
            { data: 'createTime', searchable: false},
            { data: 'createUser'},
            { data:  null, orderable:false, searchable: false}
        ],
        columnDefs : [ {
            targets : 3,
            render : function(data) {
                return moment(data).format('LLL');
            }
        },{
            targets : 5,
            data : null,
            render : function(data, type,row) {
                var html = "<a href='getBug?bugId=" + row.bugId + "' target='_blank'>查看</a>&emsp;";
                html += "<a href='javascript:removeBug(" + row.bugId + ");'> 删除</a>&emsp;";
                return html;
            }
        } ],
        pageLength: 30,
        lengthChange: false
    });

    dateTimeRange.on("apply.daterangepicker", function() {
        table.draw();
    } );

    CKEDITOR.config.defaultLanguage='zh-cn';
    CKEDITOR.replace('bugTntroEditor');

    $("#submitForm").click(function () {
        var bugIntro = $('#bugTntroEditor').val();
        var bugName = $("input[name=bugName]").val();
        if(bugIntro !== undefined && bugName!==undefined && bugIntro != null && bugName!=null && bugIntro !== '' && bugName !== ''){
            $("input[name='bugTntro']").val(bugIntro);
            $('#bugForm').submit();
        }else{
            Messager.showMsg('名称和描述不能为空');
        }
    });

    var settings = {
        idKey:'kindId',
        pIdKey:'father',
        name:'kindName',
        url:'getRootKind',
        remove:{
            url:'deleteKind',
            data:{
                kindId : 'kindId'
            },
            success:function (data) {
                ZtreeResults.removeResult = data;
                if(!data){
                    Messager.showMsg("请先删除该节点下的BUG或子节点。")
                }
            }
        },
        add:{
            url:'addKind',
            data:{
                father:'kindId',
                kindName:'newName'
            },
            success:function (data) {
                ZtreeResults.addResult = true;
                ZtreeResults.newNode.kindId = data;
            }
        },
        rename:{
            url:'addKind',
            data:{
                kindId:'kindId',
                kindName: 'newName'
            },
            success:function () {
                ZtreeResults.renameResult = true;
            }
        }
    };
    iniZtree($("#kindTree"),settings);

} );
function removeBug(id){
    $.get("removeBug.do?bugId=" + id ,function (data) {
        if(Boolean(data) === true){
            Messager.showMsg('删除成功');
        }else{
            Messager.showMsg('删除失败，该BUG包含文档');
        }
        table_id_example.DataTable().draw();
    });
}
