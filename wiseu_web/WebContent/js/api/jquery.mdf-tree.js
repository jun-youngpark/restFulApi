///////////////////////////////////////////////////////////////////////////////
// 트리
var mdf = mdf || function($){};
mdf.Tree = function(treeSelector, option) {
    this.init(treeSelector, option);
    this.initEventBind();
};

$.extend(mdf.Tree.prototype, {
    /**
     * 생성자
     *
     * @param treeSelector 트리 셀렉터
     * @param option 옵션
     */
    init : function(treeSelector, option) {
        this.option = $.extend(true, {
            treeSelector : treeSelector,
            context : '',
            new_node : $.i18n.prop("common.folder.new"),  // 새 폴더
            root_node_id : '',
            root_node_name : '',
            loadRoot : false,
            selectedNodeId : null,
            contextmenu : {
                view : {
                    create : "ROOT,INTERNAL,LEAF"
                }
            },
            plugins : ["dnd", "search", "state", "types", "unique", "wholerow"]
        }, option || {});

        var option = this.option;
        $(treeSelector).jstree({
            core : {
                animation : 200,
                strings : {new_node : this.option.new_node}, // ###
                force_text : true,  // 노드명 HTML 이스케이프
                check_callback : function (operation, node, parent, position, more) { // create, rename, move, delete 허용
                    if(operation === "copy_node" || operation === "move_node") {
                        if(parent.id === "#") {
                            return false; // 자식이 루트 위나 아래로 이동하지 못하도록 방지
                        }
                    }
                    return true;
                },
                themes : {
                    responsive : false,
                    dots : true
                    /*themes : "default",
                    icons : true,
                    url : this.option.context + "/plugin/tree/themes/default/style.css"*/
                },
                data : {
                    url : function(node) {
                        if(node.id == "#" || option.command.child.paramYn=="N") {
                            $.mdf.log(option.command.child.url);
                            return option.command.child.url;
                        }else{
                            $.mdf.log(option.command.child.url +"?"+option.command.child.param +"=" + node.id);
                            return option.command.child.url +"?"+option.command.child.param +"=" + node.id;
                        }
                    },
                    data : function(node) {
                        return {id : node.id};
                        //return node;
                    },
                    cache : false
                }
            },
            plugins : option.plugins,
            types : {
                "#" : {
                  max_children : -1,
                  max_depth : -1,
                  valid_children : ["LEAF", "INTERNAL", "SYSTEM"]
                },
                ROOT : {
                    valid_children : ["LEAF", "INTERNAL", "SYSTEM"]
                },
                INTERNAL : {
                    valid_children : ["LEAF", "INTERNAL"],
                },
                SYSTEM : {
                    valid_children : []
                },
                LEAF : {
                    valid_children : ["LEAF"]
                }
            },
            contextmenu : {
                items : function(node) {
                    if(option.contextmenu == null) {
                        return null;
                    }

                    var tree = $(treeSelector).jstree(true);

                    var nodeType = (node.id == option.root_node_id) ? 'ROOT' : 'LEAF';
                    var ctx;
                    switch(nodeType) {
                    case 'ROOT' :
                        ctx = {
                            create : {
                                label : $.i18n.prop("button.regist"),  // 등록
                                action: function (obj) {
                                    node = tree.create_node(node, {text: $.i18n.prop("common.folder.new"), type: 'LEAF'});  // 새 폴더
                                    tree.deselect_all();
                                    tree.select_node(node);
                                }
                            },
                            rename : {
                                label : $.i18n.prop("common.rename"), separator_before : true, separator_after : true,  // 이름 바꾸기
                                action: function (obj) {
                                    tree.edit(node);
                                }
                            }
                        };

                        return ctx;
                    case 'LEAF' :
                        ctx = {
                            create : {
                                label : $.i18n.prop("button.regist"),  // 등록
                                action: function (obj) {
                                    tree.create_node(node, {text: $.i18n.prop("common.folder.new"), type: 'LEAF'});  // 새 폴더
                                }
                            },
                            rename : {
                                label : $.i18n.prop("common.rename"), separator_before : true, separator_after : true,  // 이름 바꾸기
                                action: function (obj) {
                                    tree.edit(node);
                                }
                            },
                            remove : {
                                label : $.i18n.prop("button.delete"),  // 삭제
                                action: function (obj) {
                                    if(confirm("[" + node.text + "] " + $.i18n.prop("common.alert.4"))) {  // 삭제 하시겠습니까?
                                        tree.delete_node(node);
                                    }
                                }
                            }
                        }

                        return ctx;
                    }
                }
            },
        });
    },
    /**
     * 이벤트 바인딩 초기화
     */
    initEventBind : function() {
        var option = this.option;

        $(this.option.treeSelector)
        .on("select_node.jstree", function (event, data) {
            $.mdf.log("=====on.select_node.jstree");
            $.mdf.log(data);
            var nodeId = data.node.id;
            if(option.selectedNodeId && option.selectedNodeId != nodeId) {
                //$(this).jstree("deselect_node", $("#" + option.selectedNodeId));
                data.instance.deselect_node($("#" + option.selectedNodeId));
            }

            option.selectedNodeId = nodeId;
            //var children = $(this).jstree("get_children_dom", "#" + nodeId);
            var children = data.instance.get_children_dom("#" + nodeId);
            if ($.isFunction(option.command.select_node.callback)) {
                option.command.select_node.callback.call(this, data);
            }
        })
        .on("create_node.jstree", function (event, data) {
            $.mdf.log("=====on.create_node.jstree");
            $.mdf.log(data);
            $.ajax({
                async : false,
                type : "POST",
                url : option.command.insert.url,
                data : {
                    "parentId" : data.parent,
                    "nodeName" : data.node.text
                },
                success : function (result) {
                    var tree = $(option.treeSelector).jstree(true);
                    if(result.code == "OK") {
                        tree.deselect_all();
                        tree.set_id(data.node, result.value);
                        tree.select_node(data.node);
                        tree.edit(data.node);
                    } else {
                        tree.delete_node(data.node);
                        alert($.i18n.prop("common.regist.fail.alert") + '\n' + $.mdf.defaultIfBlank(result.message, ""));  // 등록에 실패하였습니다.
                    }

                    if ($.isFunction(option.command.insert.callback)) {
                        option.command.insert.callback.call(this, data, result);
                    }
                }
            });
        })
        .on("delete_node.jstree", function (event, data) {
            $.mdf.log("=====on.delete_node.jstree");
            $.mdf.log(data);
            if(option.command.remove.validate) {
                var isValid = option.command.remove.validate.call();
                if(isValid == false) {
                    data.instance.refresh();
                    return;
                }
            }

            $.ajax({
                async : false,
                type : "POST",
                url : option.command.remove.url,
                data : {
                    "nodeId" : data.node.id
                },
                success : function (result) {
                    if(result.code == "FAIL") {
                        data.instance.refresh();
                        alert($.i18n.prop("common.delete.fail.alert") + '\n' + $.mdf.defaultIfBlank(result.message, ""));  // 삭제에 실패하였습니다.
                    }

                    if ($.isFunction(option.command.remove.callback)) {
                        option.command.remove.callback.call(this, data, result);
                    }
                }
            });
        })
        .on("rename_node.jstree", function (event, data) {
            $.mdf.log("=====on.rename_node.jstree");
            $.mdf.log(data);
            if($.trim(data.text) != $.trim(data.old)) {
                $.ajax({
                    async : false,
                    type : "POST",
                    url : option.command.rename.url,
                    data : {
                        "nodeId" : data.node.id,
                        "nodeName" : data.text
                    },
                    success : function (result) {
                        if(result.code == "FAIL") {
                            var tree = $(option.treeSelector).jstree(true);
                            tree.set_text(data.node, data.old);
                            alert($.i18n.prop("common.rename.fail.alert") + '\n' + $.mdf.defaultIfBlank(result.message, ""));  // 이름 바꾸기에 실패하였습니다.
                        }

                        if ($.isFunction(option.command.rename.callback)) {
                            option.command.rename.callback.call(this, data, result);
                        }
                    }
                });
            }
        })
        /*.on("move_node.jstree", function (event, data) {
            $.mdf.log("=====on.move_node.jstree");
            data.rslt.o.each(function (i) {
                $.ajax({
                    async : false,
                    type : "POST",
                    url : option.command.move_node.url,
                    data : {
                        "nodeId" : $(this).attr("id"),
                        "parentId" : data.rslt.cr === -1 ? option.root_node_id : data.rslt.np.attr("id"),
                        "sortOrder" : data.rslt.cp + i + 1
                    },
                    success : function (result) {
                        if(result.code == "OK") {
                            $(data.rslt.oc).attr("id", r.id);
                            data.instance.refresh(data.inst._get_parent(data.rslt.oc));
                        } else {
                            $.jstree.rollback(data.rlbk);
                            alert('이동에 실패하였습니다.\n' + $.mdf.defaultIfBlank(result.message, ""));
                        }

                        if ($.isFunction(option.command.move_node.callback)) {
                            option.command.move_node.callback.call(this, data, result);
                        }
                    }
                });
            });
        })
        .on("search.jstree", function (event, data) {
            $.mdf.log("=====on.search.jstree");
            if(data.rslt.nodes.length > 0) {
                var nodeId = data.rslt.nodes.parent()[0].id;
                if(option.selectedNodeId && option.selectedNodeId != nodeId) {
                    $(this).jstree("deselect_all", $("#" + option.selectedNodeId));
                }

                $.jstree._focused().select_node("#" + nodeId);
            }
        })*/
        .on("open_node.jstree", function (event, data) {
            $.mdf.log("=====on.open_node.jstree");
            $.mdf.log(data);
            event.preventDefault();
        });
    },
    setSearchKeyword : function(searchKeyword) {

        $(this.option.treeSelector).jstree("search", searchKeyword, false);
        $(this.option.treeSelector).jstree("clear_search");
    },
    selectNode : function(nodeId) {
        $.jstree._focused().select_node("#" + nodeId);
    },
    openNode : function(nodeId) {
        $(this.option.treeSelector).jstree("open_node", $("#" + nodeId));
    },
    getSelectedNodeId : function() {
        return $(this.option.treeSelector).jstree("get_selected").attr("id");
    },
    getNodeName : function(nodeId) {
        if(nodeId == this.option.root_node_id) {
            return this.option.root_node_name;
        } else {
            return $.jstree._reference("#" + nodeId).get_text();
        }
    },
    unbindAllEvent : function() {
        $(this.option.treeSelector).unbind();
    }
});
