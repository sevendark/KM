    /**
     * Created by jiyuze on 2017/8/2.
     */
var uploadFile = jQuery("#uploadFile");
var dateTimeRange = jQuery("#dateTimeRange");
var table_id_example = jQuery('#table_id_example');
var filePath = jQuery("#filePath");
var menuContent = jQuery("#menuContent");

jQuery(document).ready( function () {
    uploadFile.fileinput({
        dropZoneEnabled: false,
        uploadUrl: 'uploadFile',
        language : 'zh',
        maxFileSize : 50000, 
        uploadExtraData: function () {
            var obj = {};
            obj.path = filePath.val();
            eval('obj.' + _csrf.parameterName + '= "' + _csrf.token + '"');
            return obj;
        }
    });

    iniDateRangPicker(dateTimeRange);

    var table = table_id_example.DataTable( {
        language: dataTableTranslation,
        serverSide: true,
        processing: true,
        orderMulti: true,
        ajax: {
            url: 'indexData',
            data : function(data) {
                for (var i = 0; i < data.columns.length; i++) {
                    delete(data.columns[i].search);
                }
                data.startTime = dateTimeRange.data('daterangepicker').startDate.format('YYYY-MM-DD HH:mm')+ ':00';
                data.endTime = dateTimeRange.data('daterangepicker').endDate.format('YYYY-MM-DD HH:mm') + ':59';
            }
        },
        columns: [
            { data: 'docId'},
            { data: 'docName'},
            { data: 'createTime', searchable: false},
            { data: 'createUser'},
            { data:  null, orderable:false, searchable: false}
        ],
        columnDefs : [ {
            targets : [1,3],
            render : function(data) {
                return '<xmp>' + data + '</xmp>';
            }
        },{
            targets : 2,
            render : function(data) {
                return moment.unix(data).format('LLL');
            }
        },{
            targets : 4,
            data : null,
            render : function(data, type,row) {
                var html = "<a href='getDoc?docId=" + row.docId + "' target='_blank'>查看</a>&emsp;";
                html += "<a href='download?docId=" + row.docId + "'>下载</a>&emsp;";
                html += "<a href='javascript:deleteFileIndex(" + row.docId + ");' id='del_a_" + row.docId + "'> 删除</a>&emsp;";
                return html;
            }
        } ],
        pageLength: 30,
        lengthChange: false,
        createdRow: function(row, data, dataIndex) {
            $('td:eq(0)', row).html("<div class='details details_png'></div>&emsp;" + data.docId);
            $(row).data("intro",data.docIntro);
            $(row).find('div.details').on('click', function () {
                var tr = $(this).closest('tr');
                var row = table.row( tr );
                if ( row.child.isShown() ) {
                    row.child.hide();
                    tr.removeClass('shown');
                } else {
                    row.child( '<xmp>' + row.data().docIntro +'</xmp>').show();
                    tr.addClass('shown');
                }
            });
        }
    });

    dateTimeRange.on("apply.daterangepicker", function() {
        table.draw();
    } );
    uploadFile.on('fileuploaded', function() {
        setTimeout(function () {
            table.draw();
        },1500);
    });
    var setting = {
        check: {
            enable: true,
            chkStyle: "radio",
            radioType: "all"
        },
        view: {
            dblClickExpand: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: onClick,
            onCheck: onCheck
        }
    };
    function onClick(e, treeId, treeNode) {
        var zTree = jQuery.fn.zTree.getZTreeObj("treeDemo");
        zTree.checkNode(treeNode, !treeNode.checked, null, true);
        return false;
    }

    function onCheck() {
        var zTree = jQuery.fn.zTree.getZTreeObj("treeDemo"),
            nodes = zTree.getCheckedNodes(true),
            v = "";
        for (var i=0, l=nodes.length; i<l; i++) {
            v += nodes[i].path + ",";
        }
        if (v.length > 0 ) v = v.substring(0, v.length-1);
        filePath.attr("value", v);
    }
    filePath.click(function () {
        menuContent.slideDown("fast");
        jQuery("body").bind("mousedown", onBodyDown);
    });
    function hideMenu() {
        menuContent.fadeOut("fast");
        jQuery("body").unbind("mousedown", onBodyDown);
    }
    function onBodyDown(event) {
        if (!(event.target.id === "menuBtn" || event.target.id === "filePath" || event.target.id === "menuContent" || jQuery(event.target).parents("#menuContent").length>0)) {
            hideMenu();
        }
    }

    var folderTreeSetting = {
        remove:{
            url:'deleteFolder',
            data:{
                path : 'path'
            },
            success:function (data) {
                ZtreeResults.removeResult = data;
                if(!data){
                    Messager.showMsg("请先删除文件夹中的内容。")
                }
            }
        },
        add:{
            url:'addFolder',
            data:{
                folderName:'newName',
                name:'newName',
                path:'path'
            },
            success:function (data) {
                if(data.isSuccess){
                    ZtreeResults.addResult = data.isSuccess;
                    ZtreeResults.newNode.path = data.path;
                }
            }
        },
        rename:{
            url:'renameFolder',
            data:{
                path: 'path',
                oldName: 'name',
                newName: 'newName'
            },
            success:function (data, treeNode) {
                if(!data.key){
                    Messager.showMsg("节点更新失败，可能存在同名文件夹。");
                }
                ZtreeResults.renameResult = data.key;
                treeNode.path = data.path;
            }
        }
    };
    jQuery.ajax({
        url: "getFolderList",
        cache: false,
        success: function (zNodes) {
            jQuery.fn.zTree.init(jQuery("#treeDemo"), setting, zNodes);
            folderTreeSetting.data = zNodes;
            iniZtree(jQuery("#folderTree"), folderTreeSetting, zNodes);
        }
    });

});
function deleteFileIndex(docId){
    jQuery("#del_a_" + docId).html("删除中...");
    jQuery.post("deleteFile",{_csrf:_csrf.token,docId:docId},function(){
        table_id_example.DataTable().draw();
    });
}
var syncFlag = false;
function sync(me) {
    if(!syncFlag){
        syncFlag = true;
        jQuery(me).html('<i class="glyphicon glyphicon-refresh"></i>&nbsp;同步中...');
        jQuery.post("syncSVN",{_csrf:_csrf.token},function (data) {
            syncFlag = false;
            var html = "";
            if(data.files.length !== 0){
                html += "以下文件同步完成：";
                jQuery.each(data.files, function (i,info) {
                    html += info + "<br/>";
                })
            }else{
                html += "没有文件被同步。";
            }
            var now = moment().format('LT');
            jQuery(me).html('<i class="glyphicon glyphicon-refresh"></i>&nbsp;上一次同步时间' + now );
            Messager.showMsg(html);
            table_id_example.DataTable().draw();
        });
    }else{
        Messager.showMsg('正在同步，请勿重复点击。');
    }
}