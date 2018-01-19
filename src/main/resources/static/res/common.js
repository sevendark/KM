/**
 * Created by jiyuze on 2017/8/2.
 */
var Messager = null;
var _csrf = null;
moment.locale('zh-cn');
var dataTableTranslation = {
    "sProcessing":   "处理中...",
    "sLengthMenu":   "显示 _MENU_ 项结果",
    "sZeroRecords":  "没有匹配结果",
    "sInfo":         "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
    "sInfoEmpty":    "显示第 0 至 0 项结果，共 0 项",
    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
    "sInfoPostFix":  "",
    "sSearch":       "搜索:",
    "sUrl":          "",
    "sEmptyTable":     "表中数据为空",
    "sLoadingRecords": "载入中...",
    "sInfoThousands":  ",",
    "oPaginate": {
        "sFirst":    "首页",
        "sPrevious": "上页",
        "sNext":     "下页",
        "sLast":     "末页"
    },
    "oAria": {
        "sSortAscending":  ": 以升序排列此列",
        "sSortDescending": ": 以降序排列此列"
    }
};
iniDateRangPicker=function($div){
    $div.daterangepicker({
        timePicker: true,
        timePickerIncrement: 1,
        timePicker24Hour: true,
        locale: {
            applyLabel: '确认',
            cancelLabel: '取消',
            fromLabel : '起始时间',
            toLabel : '结束时间',
            customRangeLabel : '自定义',
            firstDay : 1,
            format: 'YYYY-MM-DD HH:mm',
            separator : ' 至 '
        },
        ranges : {
            '最近1小时': [moment().subtract(1,'hours'), moment()],
            '今日': [moment().startOf('day'), moment()],
            '昨日': [moment().subtract(1,'days').startOf('day'), moment().subtract(1,'days').endOf('day')],
            '最近7日': [moment().subtract(6,'days'), moment()],
            '最近30日': [moment().subtract(29,'days'), moment()],
            '本月': [moment().startOf("month"),moment().endOf("month")],
            '上个月': [moment().subtract(1,"month").startOf("month"),moment().subtract(1,"month").endOf("month")]
        },
        startDate: moment().subtract(29,'days'),
        endDate: moment().add(1,'days')
    });
};

var ZtreeResults = {
    removeResult : false,
    renameResult : false,
    addResult : false,
    newNode : {}
};

/***
 *
 * @param ztreeDom
 * @param settings
 * var settings = {
        idKey:'',
        pIdKey:'',
        name:'',
        url:'',
        remove:{
            url:'',
            data:{},
            success:function (data) {}
        },
        add:{
            url:'',
            data:{},
            success:function (data) {}
        },
        rename:{
            url:'',
            data:{},
            success:function () {}
        }
    };
 */
iniZtree=function (ztreeDom,settings) {
    var setting = {
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            selectedMulti: false
        },
        edit: {
            enable: true,
            editNameSelectAll: true,
            showRemoveBtn: showRemoveBtn,
            showRenameBtn: showRenameBtn
        },
        data: {
            simpleData: {
                enable: true,
                idKey: settings['idKey']==null?'id':settings['idKey'],
                pIdKey: settings['pIdKey']==null?'pId':settings['pIdKey'],
                rootPId:null
            },
            key:{
                name: settings['name']==null?'name':settings['name']
            }
        },
        callback: {
            beforeDrag: beforeDrag,
            beforeEditName: beforeEditName,
            beforeRemove: beforeRemove,
            beforeRename: beforeRename,
            onRemove: onRemove,
            onRename: onRename
        }
    };

    if(settings.data){
        $.fn.zTree.init(ztreeDom, setting, settings['data']);
    }else{
        $.get(settings['url'],function (data) {
            $.fn.zTree.init(ztreeDom, setting, data);
        });
    }


    function beforeDrag(treeId, treeNodes) {
        return false;
    }

    function beforeEditName(treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj(treeId);
        zTree.selectNode(treeNode);
        setTimeout(function() {
            if (confirm("进入节点 -- " + treeNode[setting.data.key.name] + " 的编辑状态吗？")) {
                setTimeout(function() {
                    zTree.editName(treeNode);
                }, 0);
            }
        }, 0);
        return false;
    }
    function getDataField(data,treeNode) {
        var obj={_csrf : _csrf.token};
        for(var p in data){
            obj[p] = treeNode[data[p]];
        }
        return obj;
    }

    function beforeRemove(treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj(treeId);
        zTree.selectNode(treeNode);
        if(confirm("确认删除 节点 -- " + treeNode[setting.data.key.name] + " 吗？")){
            $.ajax({
                url: settings.remove.url,
                type:'post',
                data:getDataField(settings.remove.data, treeNode),
                async: false,
                success:function (data) {
                    settings.remove.success(data);
                }
            });
        }
        return ZtreeResults.removeResult;
    }
    function onRemove(e, treeId, treeNode) {
    }
    function beforeRename(treeId, treeNode, newName, isCancel) {
        if(!isCancel){
            var zTree = $.fn.zTree.getZTreeObj(treeId);
            if (newName.length === 0) {
                setTimeout(function() {
                    zTree.cancelEditName();
                }, 0);
                return ZtreeResults.renameResult;
            }
            treeNode.newName = newName;
            $.ajax({
                url: settings.rename.url,
                type:'post',
                data:getDataField(settings.rename.data, treeNode),
                async: false,
                success:function (data) {
                    settings.rename.success(data);
                }
            });
            if(ZtreeResults.renameResult === false){
                zTree.cancelEditName();
            }
            return ZtreeResults.renameResult;
        }else{
            return true;
        }
    }

    function onRename(e, treeId, treeNode, isCancel) {
    }
    function showRemoveBtn(treeId, treeNode) {
        return treeNode.level !== 0;
    }
    function showRenameBtn(treeId, treeNode) {
        return treeNode.level !== 0;
    }

    function addHoverDom(treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj(treeId);
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='add node' onfocus='this.blur();'></span>";
        sObj.after(addStr);
        var btn = $("#addBtn_"+treeNode.tId);
        if (btn) btn.bind("click", function(){
            treeNode.newName = "新节点(请修改名称，否则无法继续添加)";
            var nodes = zTree.getNodesByParam('name',treeNode.newName);
            if(nodes.length !== 0){
                Messager.showMsg("请先修改另一个新增的节点。");
                return false;
            }
            ZtreeResults.newNode = getDataField(settings.add.data, treeNode);
            $.ajax({
                url:settings.add.url,
                type:'post',
                data:ZtreeResults.newNode,
                async: false,
                success:function (data) {
                    settings.add.success(data);
                }
            });
            if(ZtreeResults.addResult){
                zTree.addNodes(treeNode, ZtreeResults.newNode);
            }
            return false;
        });
    }
    function removeHoverDom(treeId, treeNode) {
        $("#addBtn_"+treeNode.tId).unbind().remove();
    }
}


