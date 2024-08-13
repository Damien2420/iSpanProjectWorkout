$(function () {
	$('tbody tr').each(function () {
		let firstSixBox = $(this).find('td:lt(6)');
		firstSixBox.css('cursor', 'pointer');

		firstSixBox.click(function () {
			let link = $(this).find('a').attr('href');
			window.location.href = link;
		})
	});
})