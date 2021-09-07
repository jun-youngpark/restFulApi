/*-------------------------------------------------------------------------------------------------
 * 캠페인/이케어 저작기 2단계 파일첨부 (메일)
-------------------------------------------------------------------------------------------------*/
/**
 * selector의 하위 file 정보 값을 배열에 저장한다.
 */
function makeMultipart(selector, multipartFiles){
    $(selector).each(function(i) {
        multipartFiles.push({
            "fileName":$(this).find("input[name*='fileName']").val(),
            "fileAlias":$(this).find("input[name*='fileName']").val(),
            "filePath":$(this).find("input[name*='filePath']").val(),
            "fileType":$(this).find("input[name*='fileType']").val(),
            "secuField":$(this).find("input[name*='secuField']").val(),
            "encYn":$(this).find("input[name*='encYn']").is(':checked')?"Y":"N"
        })
    });
}

/**
 * 추가된 첨부파일 스크립트 핸들러 생성
 */
function multifileHandler() {
    var handlerLine = new Line($("#editorFrm textarea[name=handler]").val());
    var reHandler = "";
    var line = "";

    var isFileDir = false;
    var isFilePath = false;

    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("filedir = java.lang.StringBuffer.new()") > -1) {
            isFileDir = true;
            continue;
        } else if(line.indexOf("filepath_") > -1 && line.indexOf("java.lang.StringBuffer.new()") > -1) {
            isFileDir = false;
            isFilePath = true;
            continue;
        } else if(line.indexOf("com.mnwise.ASE.agent.fileagent.FileAgent.new") > -1) {
            isFilePath = false;
            continue;
        } else if(line.indexOf("body.addAlterContent(") > -1) {
            reHandler += line.replace("addAlterContent", "setContent");
            reHandler += "\n";
            continue;
        } else if(line.indexOf("body.addAttachment(") > -1) {
            continue;
        } else if(isFileDir == false && isFilePath == false) {
            reHandler += line;
            reHandler += "\n";
        }
    }

    // 첨부파일이 있다면 수행
    if(multipartfile.length > 0) {
        handlerLine = new Line(reHandler);
        reHandler = "";

        var fileDir = "";
        var filePath = new Array();
        var attachment = new Array();
        var space = "";
        var fileNo = 0;

        // 1. 업로드파일 절대경로
        // 캠페인, 이케어 번호 동일시 파일첨부 문제되는 부분 경로 수정
        if ($.mdf.isBlank($("editorFrm input[name=serviceType]").val())) {
            fileDir = 'filedir = java.lang.StringBuffer.new()'
                + '\nfiledir.append(context.get("_SERVER_ROOT"))'
                + '\nfiledir.append("/spool/mts/fileupload/EM/")'
                + '\nfiledir.append(context.get("_CAMPAIGN_NO"))'
                + '\nfiledir.append("/")';
        } else {
            fileDir = 'filedir = java.lang.StringBuffer.new()'
                + '\nfiledir.append(context.get("_SERVER_ROOT"))'
                + '\nfiledir.append("/spool/mts/fileupload/EC/")'
                + '\nfiledir.append(context.get("_CAMPAIGN_NO"))'
                + '\nfiledir.append("/")';
        }

        $.each(multipartfile, function(key, val) {
            if(val != null) {
                fileNo++;

                // 2. 첨부파일 절대경로
                filePath[fileNo - 1] = '\nfilepath_' + fileNo + ' = java.lang.StringBuffer.new()'
                    + '\nfilepath_' + fileNo + '.append(filedir.toString())'
                    + '\nfilepath_' + fileNo + '.append("' + multipartfile[key].fileAlias + '")'
                    + '\nfile_' + fileNo + ' = com.mnwise.ASE.agent.fileagent.FileAgent.new(filepath_' + fileNo + '.toString())';

                // body 에 addAttachment 추가할 핸들러 작성
                attachment[fileNo - 1] = 'body.addAttachment("' + multipartfile[key].fileName + '", '
                    + 'file_' + fileNo + ".toByteArray())";
            }
        });

        if(filePath.length > 0) {
            // 3. 핸들러에 추가
            while((line = handlerLine.readLine()) != null) {
                if(line.indexOf("message.prepareTemplate()") > -1) {
                    reHandler += line + "\n" + fileDir; // 업로드파일 절대경로

                    // 첨부파일 절대경로
                    for(var i = 0; i < filePath.length; i++) {
                        reHandler += filePath[i];
                    }
                } else if(line.indexOf("com.mnwise.ASE.agent.mailagent.MailBody.new(context)") > -1) {
                    reHandler += line.replace("MailBody", "MailBody2");
                } else if(line.indexOf("body.setContent") > -1) {
                    space = line.substring(0, line.indexOf("body.setContent"));
                    reHandler += line.replace("setContent", "addAlterContent");

                    // addAttachment
                    for(var i = 0; i < attachment.length; i++) {
                        reHandler += "\n" + space + attachment[i];
                    }
                } else {
                    reHandler += line;
                }

                reHandler += "\n";
            }

            $("#editorFrm textarea[name=handler]").val(reHandler);
            return;
        }
    }

    $("#editorFrm textarea[name=handler]").val(handlerLine.toString());
}

/**
 * 그루비 첨부파일 핸들러 생성
 */
function multifileGroovyHandler() {
    var handlerLine = new Line($("#editorFrm textarea[name=handler]").val());
    var reHandler = "";
    var line = "";

    var isFileDir = false;
    var isFilePath = false;

    // 첨부파일 핸들러를 지워서 파일 개수에 따라 다시 핸들러를 생성한다.
    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("import com.mnwise.ASE.agent.mailagent.MailBody2") > -1) {
            reHandler += line.replace("MailBody2", "MailBody");
            reHandler += "\n";
        } else if(line.indexOf("import com.mnwise.ASE.agent.fileagent.FileAgent;") > -1 )  {
            continue;
        } else if(line.indexOf("import java.text.SimpleDateFormat;") > -1 && $("#editorFrm input[name=campaignPreface]").val() != undefined )  {
            continue;
        } else if(line.indexOf("StringBuffer filedir = new StringBuffer()") > -1) {
            isFileDir = true;
            continue;
        } else if(line.indexOf("StringBuffer filepath_") > -1 && line.indexOf("new StringBuffer()") > -1) {
            isFileDir = false;
            isFilePath = true;
            continue;
        } else if(line.indexOf("FileAgent file_") > -1 && line.indexOf("new FileAgent(") > -1) {
            isFilePath = false;
            continue;
        } else if(line.indexOf("MailBody2 body = new MailBody2(context)") > -1) {
            reHandler += line.replace("MailBody2", "MailBody").replace("MailBody2(", "MailBody(") + "\n";
        } else if(line.indexOf("body.addAlterContent(") > -1) {
            reHandler += line.replace("addAlterContent", "setContent") + "\n";
        } else if(line.indexOf("body.addAttachment(") > -1) {
            continue;
        } else if(isFileDir == false && isFilePath == false) {
            reHandler += line + "\n";
        }
    }

    // 첨부파일이 있다면 수행
    if(multipartfile.length > 0) {
        handlerLine = null;
        handlerLine = new Line(reHandler);
        reHandler = "";

        var fileDir = "";
        var filePath = new Array();
        var attachment = new Array();
        var space = "";
        var fileNo = 0;
        // 1. 업로드파일 절대경로
        // 캠페인, 이케어 번호 동일시 파일첨부 문제되는 부분 경로 수정
        if ($.mdf.isBlank($("editorFrm input[name=serviceType]").val())) {
            fileDir = 'StringBuffer filedir = new StringBuffer()'
                + '\nfiledir.append(context.get("_SERVER_ROOT"))'
                + '\nfiledir.append("/spool/mts/fileupload/EM/")'
                + '\nfiledir.append(context.get("_CAMPAIGN_NO"))'
                + '\nfiledir.append("/")';
                //+ '\nDate fileDt = new Date()'
                //+ '\nSimpleDateFormat current_date = new SimpleDateFormat("yyyyMM")'
                //+ '\nfiledir.append(current_date.format(fileDt))'
                //+ '\nfiledir.append("/")';
        } else {
            fileDir = 'StringBuffer filedir = new StringBuffer()'
                + '\nfiledir.append(context.get("_SERVER_ROOT"))'
                + '\nfiledir.append("/spool/mts/fileupload/EC/")'
                + '\nfiledir.append(context.get("_CAMPAIGN_NO"))'
                + '\nfiledir.append("/")';
        }

        $.each(multipartfile, function(key, val) {
            if(val != null) {
                fileNo++;

                // 2. 첨부파일 절대경로
                filePath[fileNo - 1] = '\nStringBuffer filepath_' + fileNo + ' = new StringBuffer()'
                    + '\nfilepath_' + fileNo + '.append(filedir.toString())'
                    + '\nfilepath_' + fileNo + '.append("' + multipartfile[key].fileAlias + '")'
                    + '\nFileAgent file_' + fileNo + ' = new FileAgent(filepath_' + fileNo + '.toString())';

                // body 에 addAttachment 추가할 핸들러 작성
                attachment[fileNo - 1] = 'body.addAttachment("' + multipartfile[key].fileName + '", '
                    + 'file_' + fileNo + ".toByteArray())";
            }
        });

        if(filePath.length > 0) {
            // 3. 핸들러에 추가
            while((line = handlerLine.readLine()) != null) {
                if(line.indexOf("import com.mnwise.ASE.agent.mailagent.MailBody") > -1) {
                    reHandler += line.replace("MailBody", "MailBody2");
                    reHandler += "\n";
                    reHandler += "import com.mnwise.ASE.agent.fileagent.FileAgent;";
                    if($("#editorFrm input[name=campaignPreface]").val() != undefined ){
                        reHandler += "\n";
                        reHandler += "import java.text.SimpleDateFormat;";
                    }
                } else if(line.indexOf("new ExceptionLogger(context)") > -1) {
                    reHandler += line + "\n" + fileDir; // 업로드파일 절대경로

                    // 첨부파일 절대경로
                    for(var i = 0; i < filePath.length; i++) {
                        reHandler += filePath[i];
                    }
                } else if(line.indexOf("new MailBody(context)") > -1) {
                    reHandler += line.replace("MailBody", "MailBody2").replace("MailBody(", "MailBody2(");
                } else if(line.indexOf("body.setContent") > -1) {
                    space = line.substring(0, line.indexOf("body.setContent"));
                    reHandler += line.replace("setContent", "addAlterContent");

                    // addAttachment
                    for(var i = 0; i < attachment.length; i++) {
                        reHandler += "\n" + space + attachment[i];
                    }
                } else {
                    reHandler += line;
                }

                reHandler += "\n";
            }

            $("#editorFrm textarea[name=handler]").val(reHandler);
            return;
        }
    }

    $("#editorFrm textarea[name=handler]").val(handlerLine.toString());
}

/**
 * 테이블을 조회해서 첨부파일 태그 생성
 * @param i
 * @return
 */
function createMultifileElement(i) {
    var fileSize = multipartfile[i].fileSize;
    var fileUnit = "B";

    while(true) {
        if(fileSize > 1024) {
            if(fileUnit == "B") {
                fileUnit = "KB";
            } else if(fileUnit == "KB") {
                fileUnit = "MB";
            } else if(fileUnit == "MB") {
                fileUnit = "GB";
            }

            fileSize = fileSize / 1024;
        } else {
            break;
        }
    }

    var insertTag = "";
    insertTag += "<div id='" + multipartfile[i].fileAlias + "' style='padding-left: 20px;'>";
    insertTag += "	<img src='/images/common/content_dot_01.gif'>";
    insertTag += multipartfile[i].fileName + " [" + parseInt(fileSize) + fileUnit + "] ";
    insertTag += "	<a href=\"javascript:deleteMultifile('" + multipartfile[i].fileAlias + "');\">";
    insertTag += "		<img src='/images/common/delete.gif' align='absmiddle'>";
    insertTag += "	</a>";
    insertTag += "</div>";

    return insertTag;
}

/**
 * 추가된 첨부파일을 화면에 보여주는 태그 생성
 */
function insertMultifile(fileAlias, fileName, fileSize, fileUnit, msg) {
    if(msg != null && msg.length > 0) {
        alert(msg);
    } else {
        multipartfile[multipartfile.length] = {
            fileAlias: fileAlias,
            fileName: fileName,
            fileSize: fileSize
        };

        var insertTag = '';
        insertTag += "<div id='" + fileAlias + "' style='padding-left: 20px;'>";
        insertTag += "	<img src='/images/common/content_dot_01.gif'>";
        insertTag += fileName + " [" + fileSize + fileUnit + "] ";
        insertTag += "	<a href=\"javascript:deleteMultifile('" + fileAlias + "');\">";
        insertTag += "		<img src='/images/common/delete.gif' align='absmiddle'>";
        insertTag += "	</a>";
        insertTag += "</div>";

        $("#multifileTagId").append(insertTag);

        $("#fileCount").text($("#multifileTagId div").length);

        if($("editorFrm input[name=handlerType]").val() == "S") {
            multifileHandler();
        } else {
            multifileGroovyHandler();
        }

        $.mdf.resizeIframe("#editorIfrm");
    }
}

/**
 * 첨부파일 삭제
 */
function deleteMultifile(fileAlias) {
    if($("editorFrm input[name=editAble]").val() == "false") {
        alert("service is running");
        return;
    }

    var no = $("#editorFrm input[name=no]").val();
    var tagType = $("#editorFrm input[name=tagType]").val();
    $("#editorFrm input[name=fileAlias]").val(fileAlias);
    if(confirm($.i18n.prop("editor.alert.multipartfile.1")) == true) {  // 선택한 첨부파일을 삭제하시겠습니까?
        /*if(tagType== 'campaign_default'){
            //EditorCampaignService.deleteEditorCampaignMultipartfile(no, fileAlias, surveyNo, deleteMultifileResult);
            sendAjaxCall("/editor/" + no + "/-1/file/c_editor.do" , "DELETE" , true , "editorFrm" , null, deleteMultifileResult);
        }else{
            //EditorEcareService.deleteEditorEcareMultipartfile(no, fileAlias, deleteMultifileResult);
            sendAjaxCall("/editor/" + no + "/-1/file/e_editor.do" , "DELETE" , true , "editorFrm" , null, deleteMultifileResult);
        }*/

        var param = {
            type : (tagType== 'campaign_default') ? "C" : "E",
            serviceNo : no,
            fileAlias : fileAlias
        };

        $.post("/editor/deleteEditorCampaignMultipartfile.json", $.param(param, true), function(result) {
            if(result.code == "OK" && result.rowCount == 1) {
                alert($.i18n.prop("editor.alert.multipartfile.3"));  // 선택한 첨부파일이 삭제되었습니다.
            } else {
                alert($.i18n.prop("editor.alert.multipartfile.2"));  // 선택한 첨부파일을 삭제하는 도중 에러가 발생하였습니다.
            }
        });

        $("#" + fileAlias.replace('.', '\\.')).remove();  // id내 .포함시 \\.로 변환
        $("#fileCount").text($("#multifileTagId div").length);

        $.each(multipartfile, function(key, val) {
            if(val != null && fileAlias == val.fileAlias) {
                multipartfile[key] = null;
            }
        });

        if($("editorFrm input[name=handlerType]").val() == "S") {
            multifileHandler();
        } else {
            multifileGroovyHandler();
        }

        $.mdf.resizeIframe("#editorIfrm");
    }
}
