<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="robots" content="noindex,nofollow"/>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Favicon -->
<link rel="icon" href="/images/favicon.png" type="image/png"/>
<!-- Icons -->
<link rel="stylesheet" href="/plugin/icon/fontawesome-free/css/all.min.css"/>
<link rel="stylesheet" href="/plugin/icon/nucleo/css/nucleo.css"/>
<!-- datatables Page plugins -->
<link rel="stylesheet" href="/plugin/tables/datatables/datatables.net-bs4/css/dataTables.bootstrap4.min.css"/>
<link rel="stylesheet" href="/plugin/tables/datatables/datatables.net-buttons-bs4/css/buttons.bootstrap4.min.css"/>
<link rel="stylesheet" href="/plugin/tables/datatables/datatables.net-select-bs4/css/select.bootstrap4.min.css"/>
<link rel="stylesheet" href="/plugin/jquery/ui/jquery-ui.css"/>
<link rel="stylesheet" href="/plugin/jquery/ui/jquery.ui.theme.css"/>
<link rel="stylesheet" href="/plugin/date/tui-date-picker-4.2.0.min.css"/>

<!--[if (gt IE 9)|!(IE)]><!-->
<link rel="stylesheet" href="/css/font.css"/>
<!--<![endif]-->

<!--[if IE 9]>
<link rel="stylesheet" href="/css/font-ie9.css"/>
<link rel="stylesheet" href="/plugin/bootstrap/css/bootstrap-ie9-4.5.0.css"/>
<link rel="stylesheet" href="/css/wiseu-ie9.css"/>
<![endif]-->

<!--[if IE 8]>
<link rel="stylesheet" href="/css/font-ie8.css"/>
<link rel="stylesheet" href="/plugin/bootstrap/css/bootstrap-ie8-4.5.0.css"/>
<![endif]-->

<!-- CSS Files -->

<link rel="stylesheet" href="/css/argon.css"/>
<link rel="stylesheet" href="/css/wiseu.css"/>

<!-- jquery -->
<script src="/plugin/jquery/jquery-1.12.4.min.js"></script>

<!--[if !IE]>-->
<script src="/plugin/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/plugin/date/tui-date-picker-4.2.0.min.js"></script>
<!--<![endif]-->

<!--[if !(IE 8)]>
<script src="/plugin/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/plugin/date/tui-date-picker-4.2.0.min.js"></script>
<![endif]-->

<!--[if IE 9]>
<script src="/plugin/bootstrap/js/bootstrap-ie9-4.5.0.js"></script>
<![endif]-->

<!--[if IE 8]>
<script src="/plugin/bootstrap/js/html5shiv.js"></script>
<script src="/plugin/bootstrap/js/bootstrap-ie8-4.3.1.js"></script>
<script src="/plugin/bootstrap/js/bootstrap.js"></script>
<script src="/plugin/bootstrap/js/flexibility.js"></script>
<script src="/plugin/date/tui-date-picker-4.2.0.js"></script>
<![endif]-->

<script src='/plugin/form/jquery.field-0.9.1.js'></script>
<script src='/plugin/jquery/jquery.hotkeys-0.7.8.js'></script>
<script src="/plugin/form/jquery.form-3.51.0.min.js"></script>
<script src="/plugin/form/jquery.autocomplete-1.0.2.js"></script>
<script src="/plugin/form/jquery.number-2.1.5.min.js"></script>
<script src="/plugin/form/js.cookie-1.5.1.js"></script>

<!-- jquery ui -->
<script src="/plugin/jquery/ui/jquery-ui-1.12.1.js"></script>
<script src='/plugin/jquery/i18n/jquery.ui.datepicker-ko.js'></script>

<script src="/plugin/text/jquery.i18n.properties-1.2.7.js"></script>

<!-- table -->
<script src="/plugin/tables/datatables/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="/plugin/tables/datatables/datatables.net-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="/plugin/tables/datatables/datatables.net-buttons/js/dataTables.buttons.min.js"></script>
<script src="/plugin/tables/datatables/datatables.net-buttons-bs4/js/buttons.bootstrap4.min.js"></script>
<script src="/plugin/tables/datatables/datatables.net-buttons/js/buttons.html5.min.js"></script>
<script src="/plugin/tables/datatables/datatables.net-buttons/js/buttons.flash.min.js"></script>
<script src="/plugin/tables/datatables/datatables.net-buttons/js/buttons.print.min.js"></script>
<script src="/plugin/tables/datatables/datatables.net-select/js/dataTables.select.min.js"></script>

<!-- WISEU Custom -->
<script src="/js/wiseu.js"></script>
<!-- common -->
<script src="/js/api/jquery.mdf-util.js"></script>
<script src="/js/api/jquery.mdf-date.js"></script>
<script src="/js/api/jquery.mdf-time.js"></script>
<script src="/js/api/jquery.mdf-datatable.js"></script>

<!-- editor -->
<script src='/js/editor/jquery.delay.js'></script>

<!-- timeentry  -->
<script src="/plugin/date/timeentry/jquery.plugin.js"></script>
<script src="/plugin/date/timeentry/jquery.timeentry-2.0.1.js"></script>

<!-- validate  -->
<script src="/plugin/validate/jquery.validate-1.19.3.min.js"></script>
<script src="/plugin/validate/additional-methods-1.19.3.min.js"></script>
<script src="/js/api/jquery.mdf-validate.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        changeLocale("${sessionScope.adminSessionVo.language}");
        initValidator();  // call by jquery.mdf-validate.js
    });

    function changeLocale(lang) {
        if ($.mdf.isBlank(lang)) {
            lang = 'ko';
        }

        Cookies.set("lang", lang, {path:"/", expires: 30});

        jQuery.i18n.properties({
            name: ['account','button','campaign','common','ecare','editor','env','report','resend','segment','template','watch','valid'],
            path: '/i18n/',
            mode: 'map',
            language: lang,
            cache : true,
            callback: function() {
            }
        });
    }

</script>
