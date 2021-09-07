<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/include/taglibs.jsp" %>
    <head>
        <script type="text/javascript">
        $(function() {
            $('td').on('click', '#delBtn', function() {
                var tr = $(this).closest("tr");
                var next = tr.next('tr');
                if (next[0] !== undefined && next[0].id === 'kakaoButtonSubList') {
                    next.remove();
                }
                tr.remove();
            });
        });

        function makeKakaoButton(linkType , type) {
            var elementStr = '';
            var typeLabel = '';
            var typeValue = '';
            var subLabel1 = '';
            var subLabel2 = '';
            var colspan = '1';
            var rowspan = '1';
            var typeNamelabel = '<spring:message code="template.kakao.button.type"/>';
            var nameLabel = '<spring:message code="template.kakao.button.name"/>';
            if(type === 'quickReplies'){
            	typeNamelabel = '<spring:message code="template.kakao.quick.replies.type"/>';
                  nameLabel = '<spring:message code="template.kakao.quick.replies.name"/>';
            }

            if(linkType === 'WL') {
                typeLabel = '<spring:message code="template.kakao.link.type.WL" />';
                typeValue = 'WL';
                subLabel1 = '<spring:message code="template.kakao.link.mobile" />';
                subLabel2 = '<spring:message code="template.kakao.link.pc" />';
            } else if(linkType === 'AL') {
                typeLabel = '<spring:message code="template.kakao.link.type.AL" />';
                typeValue = 'AL';
                subLabel1 = '<spring:message code="template.kakao.link.ios" />';
                subLabel2 = '<spring:message code="template.kakao.link.android" />';
                subLabel3 = '<spring:message code="template.kakao.link.mobile" />';
                subLabel4 = '<spring:message code="template.kakao.link.pc" />';
                rowspan = '2';
            } else if(linkType === 'DS') {
                typeLabel = '<spring:message code="template.kakao.link.type.DS" />';
                typeValue = 'DS';
                colspan = '5';
            } else if(linkType === 'BK') {
                typeLabel = '<spring:message code="template.kakao.link.type.BK" />';
                typeValue = 'BK';
                colspan = '5';
            } else if(linkType === 'MD') {
                typeLabel = '<spring:message code="template.kakao.link.type.MD" />';
                typeValue = 'MD';
                colspan = '5';
            } else if(linkType === 'BT') {
                typeLabel = '<spring:message code="template.kakao.link.type.BT" />';
                typeValue = 'BT';
                colspan = '5';
            } else if(linkType === 'AC') {
                typeLabel = '<spring:message code="template.kakao.link.type.AC" />';
                typeValue = 'AC';
                colspan = '5';
            } else if(linkType === 'BC') {
                typeLabel = '<spring:message code="template.kakao.link.type.BC" />';
                typeValue = 'BC';
                colspan = '5';
            }

            elementStr += '<tr id="kakaoButtonList" class="kakaoButtonList">'
            elementStr += '<th rowspan="' + rowspan + '">'
            elementStr += ' <button type="button" id="delBtn" class="btn btn-xs btn-outline-danger ml-2"><i class="fas fa-minus"></i></button>'
            elementStr += typeNamelabel;
            elementStr += '</th>'
            elementStr += '<td class="ls--1px" rowspan="' + rowspan + '">'+typeLabel+' <input data="linkType" name="linkType" type="hidden" value="' + typeValue + '" /></td>'
            elementStr += '<th class="ls--1px" rowspan="' + rowspan + '"><em class="required">required</em>'
            elementStr += nameLabel
           	elementStr += '</th>'

            if (linkType === 'AC' || linkType === 'BC') {
                elementStr += '<td colspan="' + colspan + '" rowspan="' + rowspan + '"><input type="text" data="name" name="name['+ ($('#kakao_button_table tr').length + 1)+']" id="name['+ ($('#kakao_button_table tr').length + 1)+']" class="form-control form-control-sm" value="' + typeLabel + '" disabled /></td>'
            } else {
                elementStr += '<td colspan="' + colspan + '" rowspan="' + rowspan + '"><input type="text" data="name" name="name['+ ($('#kakao_button_table tr').length + 1)+']" id="name['+ ($('#kakao_button_table tr').length + 1)+']" class="form-control form-control-sm" /></td>'
            }

            if(linkType === 'WL') {
                elementStr += ' <th class="ls--1px"><em class="required">required</em>'+subLabel1+'</th>'
                elementStr += ' <td><input type="text" class="form-control form-control-sm" data="linkMo" name="linkMo['+ ($('#kakao_button_table tr').length + 1)+']" id="linkMo['+ ($('#kakao_button_table tr').length + 1)+']" /></td>'
                elementStr += ' <th class="ls--1px">'+subLabel2+'</th>'
                elementStr += ' <td><input type="text" class="form-control form-control-sm" data="linkPc" name="linkPc['+ ($('#kakao_button_table tr').length + 1)+']" id="linkPc['+ ($('#kakao_button_table tr').length + 1)+']" /></td>'
            } else if(linkType === 'AL') {
                elementStr += ' <th class="ls--1px"><em class="required">required</em>'+subLabel2+'</th>'
                elementStr += ' <td><input type="text" class="form-control form-control-sm" data="linkAnd" name="linkAnd['+ ($('#kakao_button_table tr').length + 1)+']" id="linkAnd['+ ($('#kakao_button_table tr').length + 1)+']" /></td>'
                elementStr += ' <th class="ls--1px"><em class="required">required</em>'+subLabel1+'</th>'
                elementStr += ' <td><input type="text" class="form-control form-control-sm" data="linkIos" name="linkIos['+ ($('#kakao_button_table tr').length + 1)+']" id="linkIos['+ ($('#kakao_button_table tr').length + 1)+']" /></td>'
                elementStr += ' </tr>'

                elementStr += '<tr id="kakaoButtonSubList" class="kakaoButtonList">'
                elementStr += ' <th class="ls--1px"><em class="required">required</em>'+subLabel3+'</th>'
                elementStr += ' <td><input type="text" class="form-control form-control-sm" data="linkMo" name="linkMo['+ ($('#kakao_button_table tr').length + 1)+']" id="linkMo['+ ($('#kakao_button_table tr').length + 1)+']" /></td>'
                elementStr += ' <th class="ls--1px">'+subLabel4+'</th>'
                elementStr += ' <td><input type="text" class="form-control form-control-sm" data="linkPc" name="linkPc['+ ($('#kakao_button_table tr').length + 1)+']" id="linkPc['+ ($('#kakao_button_table tr').length + 1)+']" /></td>'
            }

            elementStr += ' </tr>'

            return elementStr;
        }
        </script>
    </head>
