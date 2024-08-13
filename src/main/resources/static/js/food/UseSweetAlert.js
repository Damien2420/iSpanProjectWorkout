
function showSweetAlert(title, callback) {
	Swal.fire({
		icon: "warning",
		title: title,
		showCancelButton: true, //顯示取消按鈕
		confirmButtonColor: "#3085d6",
		cancelButtonColor: "#d33",
		cancelButtonText: '取消',
		confirmButtonText: "確定刪除"
	}).then((result) => {
		if (result.isConfirmed) {
			callback();
		}
	});
}
function showSweetAlertSuccess(title, callback) {
	Swal.fire({
		icon: "success",
		title: title,
		confirmButtonColor: "#3085d6",
		confirmButtonText: "確定",

	}).finally(() => {
		callback();
	});
}