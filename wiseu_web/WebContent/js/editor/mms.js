/*--------------------------------------------------------------
 * 캠페인/이케어 저작기 2단계 이미지 처리 (MMS, 친구톡)
----------------------------------------------------------------*/
/**
 * MMS 컨텐츠 파일 타입을 얻는다.
 */
function getFileType(fileName) {
    var ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();

//	# 텍스트 영역
//	_TEXT = 00;
    if(ext == "txt") {
        return "00";
    }

//	# 이미지 영역
//	_IMAGE_PREFIX = 1
//	_IMAGE_JPEG = 10
//	_IMAGE_BMP = 11
//	_IMAGE_PNG = 12
//	_IMAGE_SIS = 13
    if(ext == "jpeg" || ext == "jpg") {
        return "10";
    } else if(ext == "bmp") {
        return "11";
    } else if(ext == "png") {
        return "12";
    } else if(ext == "sis") {
        return "13";
    }

//	# 오디오 영역
//	_AUDIO_PREFIX = 2
//	_AUDIO_MA3 = 21
//	_AUDIO_MA5 = 22
//	_AUDIO_SKM = 30
    if(ext == "ma3") {
        return "21";
    } else if(ext == "ma5") {
        return "22";
    } else if(ext == "skm") {
        return "30";
    }
}

/**
 * 핸들러에 MMS 컨텐츠 내용을 추가한다.
 */
function addMmsContentsHandler(template) {
    var index = -1;
    var offset = -1;
    var contents = "";
    var addHandler = "";

    if(template == null) {
        template = $("#editorFrm textarea[name=template]").val();
    }

    while(true) {
        index = template.indexOf("<img");
        offset = template.indexOf(">");

        if(index > -1 && offset > -1) {
            contents = template.substring(index, offset + 1);

            for(var i = 0; i < addMmsContents.length; i++) {
                // <img src=""> 태그의 src 안의 내용에 MMS 컨텐츠 파일이름이 존재한다면 핸들러 생성
                if(contents.indexOf(addMmsContents[i].fileName) > -1) {
                    addHandler += 'mmsagent.addContents("';
                    addHandler += getFileType(addMmsContents[i].fileName);
                    addHandler += '", "' + addMmsContents[i].filePath;
                    addHandler += '", "' + addMmsContents[i].fileName;
                    addHandler += '", "' + addMmsContents[i].fileSize;
                    addHandler += '")\n';
                    break;
                }
            }
        } else {
            break;
        }

        template = template.substring(offset + 1, template.length);
    }

    var handlerLine = new Line($("#editorFrm textarea[name=handler]").val());
    var line = "";
    var reHandler = "";

    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf('msg = message.execTemplate()') > -1 || line.indexOf('msg = message.execGroovyTemplate()') > -1) {
            reHandler += addHandler + line + '\n';
        } else if(line.indexOf("mmsagent.addContents(") > -1) {
            continue;
        } else {
            reHandler += line + '\n';
        }
    }

    $("#editorFrm textarea[name=handler]").val(reHandler);
}

/**
 * 선택한 컨텐츠를 모바일 화면에 넣는다.
 */
var addMmsContents = new Array();
function insertMmsContents(filePath, fileName, fileSize) {
    addMmsContents[addMmsContents.length] = {
        filePath : filePath,
        fileName : fileName,
        fileSize : fileSize
    };
}

/**
 * 등록된 핸들러에서 컨텐츠를 조회한다.
 */
function getContents() {
    var handlerLine = new Line($("#editorFrm textarea[name=handler]").val());
    var line = "";

    while((line = handlerLine.readLine()) != null) {
        if(line.indexOf("mmsagent.addContents(") > -1) {
            line = line.substring(line.indexOf("mmsagent.addContents(") + "mmsagent.addContents(".length, line.length - 1);
            var addContentsArr = line.split(",");

            var filePath = $.trim(addContentsArr[1]).replace('\"', '').replace('\"', '');

            if(addContentsArr.length == 4) {
                insertMmsContents(filePath, $.trim(addContentsArr[2]), $.trim(addContentsArr[3]));

                //EditorMmsService.selectEditorMmsContentsFilePreviewPath(filePath, getEditorMmsContentsFilePreviewPath);
                $.post("/editor/selectEditorMmsContentsFilePreviewPath.json?filePath=" + filePath, null, function(filePreviewPath) {
                    getEditorMmsContentsFilePreviewPath(filePreviewPath);
                });
            }
        }
    }
}

function getEditorMmsContentsFilePreviewPath(filePreviewPath) {
    insertMmsTemplateContents(filePreviewPath);
}