function deleteAlert() {
    Swal.fire({
        icon: 'warning',
        title: '確定要刪除商品資料嗎',
        text: '此動作無法回復！',
        showCancelButton: true,
        confirmButtonText: "確定",
        cancelButtonText: "取消",
        reverseButtons: true,
        showClass: {
            popup: 'animate__animated animate__fadeInDown animate__faster'
        },
        hideClass: {
            popup: 'animate__animated animate__fadeOutUp animate__faster'
        }
    }).then(result => {
        if (result.isConfirmed) {
            const deleteCheckbox = document.querySelectorAll('input[name="delete"]:checked');
            const deleteData = {};

            deleteCheckbox.forEach((checkbox, key) => {
                const index = (key + 1).toString();
                deleteData[index] = checkbox.value;
            });
            const dataJson = JSON.stringify(deleteData);
            axios.post('delete', dataJson, {
                headers: { 'Content-Type': 'application/json' }
            }).then(response => {
                deleteRow();
                deleteCheckbox.forEach(checkbox => {
                    if (checkbox.checked) {
                        const row = checkbox.closest('tr');
                        row.remove();
                    }
                });
                Swal.fire({
                    icon: 'success',
                    title: '成功刪除' + response.data + '筆資料',
                    timer: 2000,
                    timerProgressBar: true,
                    toast: true,
                    showConfirmButton: false,
                    position: 'top'
                });
            }).catch(error => {
                Swal.fire({
                    icon: 'warning',
                    title: '刪除失敗',
                    text: error,
                    showCloseButton: true,
                    showConfirmButton: true
                });
                console.log(error);
            });
        } else if (result.dismiss === Swal.DismissReason.cancel) {
            uncheck();
            deleteRow();
        }
    });
}