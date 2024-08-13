$(document).ready(function() {
    $('#memberTable').DataTable();
});

$(function () {
    $('.dropdown-toggle').click(function () {
        $(this).next('ul').toggleClass('active');
        // $(this).next('ul').slideToggle(300, "linear");
    });
});

$('table').on('click', '#block', function () {
	$(this).closest('tr').remove();
})


