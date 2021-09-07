(function() {
    tinymce.create('tinymce.plugins.MnwisePlugin', {
        createControl: function(n, cm) {
            switch (n) {
            case 'sematic':
                var mlb = cm.createListBox('sematic', {
                    title : $.i18n.prop("editor.option.person"),
                    onselect : function(v) {
                        var isRealtime = false;
                        try{
                            isRealtime=document.editorFrm.serviceType.value==("R");
                        } catch(E){
                            isRealtime = false;
                        }

                        if(semantic != null && v != "" && semantic.length > 0) {
                            var value = "";
                            if(isTitleFocus == true) { // 제목에 개인화 인자를 추가할 경우
                                if(isRealtime){
                                    value = "<%=" + semantic[v - 1].itemNm + "=%>";
                                } else { // 캠페인
                                    value = "<%=" + semantic[v - 1].fieldDesc + "=%>";
                                }
                                insertAtCaret(document.editorFrm.campaignPreface, value);

                            } else { // 에디터에 개인화 인자를 추가할 경우
                                if(isRealtime){
                                    value = "{$" + semantic[v - 1].itemNm + "$}";
                                } else if(semantic[v - 1].fieldKey == null || semantic[v - 1].fieldKey != 'D') {  // 전문인자가 아닌 경우
                                    value = "{$" + semantic[v - 1].fieldDesc + "$}";
                                }
                                tinyMCE.execCommand('mceInsertContent', false, value);
                            }
                        }
                    }});

                    if(semantic.length == 0) {
                        mlb.add("No personalization", -1);
                    } else {
                        var isRealtime = false;
                        try{
                            isRealtime=document.editorFrm.serviceType.value==("R");
                        } catch(E){
                            isRealtime = false;
                        }

                        for(var i = 0; i < semantic.length; i++) {
                            if(semantic[i].fieldKey == 'D') {
                                mlb.add(semantic[i].fieldNm, semantic[i].fieldSeq);
                            } else {
                                if(isRealtime){
                                    mlb.add(semantic[i].itemNm, i+1);
                                } else {
                                    mlb.add(semantic[i].fieldDesc, semantic[i].fieldSeq);
                                }
                            }
                        }
                    }

                    return mlb;

            case 'file':
                var c = cm.createMenuButton('file', {
                    title : $.i18n.prop("editor.import"),  // 불러오기
                    image : '/images/editor/open.gif',
                    icons : false
                });

                c.onRenderMenu.add(function(c, m) {
                    m.add({title : $.i18n.prop("editor.import.zip"), onclick : function() {  // ZIP 파일 불러오기
                        var type = "EM";
                        var uploadType = "template";
                        var no = document.editorFrm.no.value;

                        // /jsp/editor/upload_editor.jsp
                        window.open('/jsp/editor/importPopup_file.jsp?type=' + type + '&uploadType=' + uploadType + '&no=' + no,'UPLOAD','menubar=no,toolbar=no,location=no,status=no,scrollbars=yes,resizable=yes,width=560,height=260');
                    }});

                    m.add({title : $.i18n.prop("editor.import.html"), onclick : function() {  // HTML 파일 불러오기
                        // /jsp/editor/upload_editor.jsp
                        window.open('/jsp/editor/importPopup_file.jsp?type=html&uploadType=html','UPLOAD','menubar=no,toolbar=no,location=no,status=no,scrollbars=yes,resizable=yes,width=560,height=260');
                    }});

                    m.add({title : $.i18n.prop("editor.import.template"), onclick : function() {  // 템플릿 불러오기
                        // /editor/template_editor.do
                        window.open('/editor/importTemplateListPopup.do','TEMPLATE','menubar=no,toolbar=no,location=no,status=no,scrollbars=no,resizable=no,width=840,height=510');
                    }});

                    m.add({title : $.i18n.prop("editor.import.url"), onclick : function() {  // URL 불러오기
                        // /jsp/editor/url_editor.jsp
                        window.open('/jsp/editor/importPopup_url.jsp','URL','menubar=no,toolbar=no,location=no,status=no,scrollbars=yes,resizable=yes,width=560,height=260');
                    }});

                    m.add({title : $.i18n.prop("editor.import.attach"), onclick : function() {  // 파일 첨부하기
                        var type = "EM";
                        var uploadType = "file";
                        var no = document.editorFrm.no.value;

                        // /jsp/editor/upload_editor.jsp
                        window.open('/jsp/editor/importPopup_file.jsp?type=' + type + '&uploadType=' + uploadType + '&no=' + no,'UPLOAD','menubar=no,toolbar=no,location=no,status=no,scrollbars=yes,resizable=yes,width=560,height=260');
                    }});
                });

                return c;

            case 'linktrace':
                var c = cm.createMenuButton('linktrace', {
                    title : $.i18n.prop("editor.link"),  // 링크
                    image : '/images/editor/link.gif',
                    icons : false
                });

                c.onRenderMenu.add(function(c, m) {
                    m.add({title : $.i18n.prop("editor.link.hyper"), onclick : function() {  // 하이퍼링크
                        var element = tinyMCE.get('template').selection.getNode().nodeName;
                        var hyperlink = tinyMCE.get('template').selection.getContent();

                        if(element == 'A' && hyperlink.length > 0) {
                            if(confirm($.i18n.prop("editor.alert.hyper.confirm")) == true) {  // 하이퍼링크가 설정되어 있습니다. 삭제하시겠습니까?
                                tinyMCE.execCommand('unlink', false, hyperlink);
                            }
                        } else {
                            if(hyperlink.length > 0) {
                                hyperlink = encodeURIComponent(hyperlink);
                                // /jsp/editor/hyperlink_editor.jsp
                                window.open('/jsp/editor/linkPopup_hyperlink.jsp?hyperlink=' + hyperlink, 'HYPERLINK','menubar=no,toolbar=no,location=no,status=no,scrollbars=yes,resizable=yes,width=520,height=140');
                            } else {
                                alert($.i18n.prop("editor.alert.hyper.click"));  // 하이퍼링크 할 영역을 선택하여 주십시오.
                            }
                        }
                    }});

                    m.add({title : $.i18n.prop("editor.link.tracelist"), onclick : function() {  // 링크추적리스트
                        if(semantic.length == 0) {
                            alert($.i18n.prop("editor.alert.editor.3"));  // 대상자를 먼저 등록하신 후 링크를 설정하실 수 있습니다.
                        } else {
                            linktrace = new Linktrace();
                            window.open('/jsp/editor/linktrace_editor.jsp','LINKTRACE','menubar=no,toolbar=no,location=no,status=no,scrollbars=yes,resizable=no,width=1024,height=768');
                        }
                    }});
                });

                return c;

            case 'reject':
                var c = cm.createMenuButton('reject', {
                    title : $.i18n.prop("report.campaign.list.title.reject"),  // 수신거부
                    image : '/images/editor/reject.png',
                    icons : false,
                    onclick : function() {
                        if(semantic.length == 0) {
                            alert($.i18n.prop("editor.alert.editor.4"));  // 대상자를 먼저 등록하신 후 수신거부를 설정하실 수 있습니다.
                            return;
                        }

                        var rejectLink = "<%=(Reject)%>";

                        var rejectSelect = tinyMCE.get('template').selection.getContent();
                        var parentRejectSelect = tinyMCE.get('template').selection.getNode().parentNode.parentNode.innerHTML;

                        if((rejectSelect != null && rejectSelect.indexOf(rejectLink) > -1)
                            || (parentRejectSelect != null && parentRejectSelect.indexOf(rejectLink) > -1)) {
                            if(confirm($.i18n.prop("editor.alert.editor.5")) == true) {  // 수신거부가 설정되어 있습니다. 해제하시겠습니까?
                                tinyMCE.execCommand('unlink', false, tinyMCE.get('template').selection.getContent());
                            }
                        } else {
                            if(rejectSelect != null && rejectSelect.length > 0) {
                                if(confirm($.i18n.prop("editor.alert.editor.6")) == true) {  // 선택한 영역에 수신거부를 설정하시겠습니까?
                                    tinyMCE.execCommand('createlink', false, rejectLink);
                                }
                            } else {
                                alert($.i18n.prop("editor.alert.editor.7"));  // 수신거부 할 영역을 선택하여 주십시오.
                            }
                        }
                    }
                });

                return c;

            case 'seg':
                var mlb = cm.createListBox('seg', {
                    title : $.i18n.prop("editor.template.select"),  // 템플릿선택
                    onselect : function(v) {
                        if(v != "" && templateArr.length > 0) {
                            var seg = document.editorFrm.seg.value;

                            if(confirm((seg == " " ? $.i18n.prop("env.menu.handler") : seg) + " " + $.i18n.prop("editor.alert.editor.22")) == true) {  // 기본, 템플릿을 저장하시겠습니까?
                                var no = document.editorFrm.no.value;
                                var segmentNo = document.editorFrm.segmentNo.value;
                                var template = tinyMCE.get('template').getContent();
                                var handlerType = document.editorFrm.handlerType.value;

                                var param = {
                                    no : no,
                                    segmentNo : segmentNo,
                                    template : template,
                                    seg : seg,
                                    handlerType : handlerType
                                };

                                for (var i = 0; i < templateArr.length; i++) {
                                    if(seg == templateArr[i].seg) {
                                        //EditorCampaignService.updateEditorCampaignTemplate(no, segmentNo, template, seg, handlerType);
                                        $.post("/editor/updateEditorCampaignTemplate.json", $.param(param, true), function(result) {
                                        });
                                        templateArr[i].template = template;
                                        break;
                                    }
                                }
                            }

                            document.editorFrm.segIndex.value = v - 1;
                            document.editorFrm.seg.value = templateArr[v - 1].seg;
                            tinyMCE.get('template').setContent(templateArr[v - 1].template);
                        }
                    }
                });

                if(templateArr.length == 0) {
                    mlb.add($.i18n.prop("env.menu.handler"), (i + 1));  // 기본
                } else {
                    for(var i = 0; i < templateArr.length; i++) {
                        if(templateArr[i].seg == ' ') {
                            mlb.add($.i18n.prop("env.menu.handler"), (i + 1));  // 기본
                        } else {
                            mlb.add(templateArr[i].seg, (i + 1));
                        }
                    }
                }

                return mlb;

            case 'segItem':
                var c = cm.createMenuButton('segItem', {
                    title : $.i18n.prop("editor.template.multi"),  // 멀티템플릿
                    image : '/images/editor/seg.gif',
                    icons : false
                });

                c.onRenderMenu.add(function(c, m) {
                    m.add({title : $.i18n.prop("editor.template.add"), onclick : function() {  // 템플릿추가
                        document.getElementById('segItem').style.display = 'block';
                        document.getElementById('segValue').focus();
                    }});

                    m.add({title : $.i18n.prop("editor.template.delete"), onclick : function() {  // 템플릿삭제
                        deleteSeg(document.editorFrm.seg.value);
                    }});
                });

                return c;

            case 'htmlToggle':
                var c = cm.createMenuButton('htmlToggle', {
                    title : $.i18n.prop("editor.editmod.change"),  // 편집모드전환
                    image : '/images/editor/HTMLEditorButton.png',
                    icons : false,
                    onclick : function() {
                        if(null!=cm && null!=cm.editor){
                            tinyMCE.execCommand('mceToggleEditor',false, cm.editor.editorId);
                            $("#htmlToggleTr").css("display","inline-block");
                        }
                    }
                });
                return c;
            }

            return null;
        }
    });

    tinymce.PluginManager.add('mnwise', tinymce.plugins.MnwisePlugin);
})();