const updateSubmitButton = document.querySelector('.update-submit');

const updateSwal = Swal.mixin({
    showCancelButton: true,
    confirmButtonColor: "#f3c449",
    cancelButtonColor: "#383737",
    confirmButtonText: "確定",
    cancelButtonText: "取消",
    reverseButtons: true,
    customClass: {
        confirmButton: 'swalConfirmBtn',
        cancelButton: 'swalCancelBtn'
    },
});

updateSubmitButton.addEventListener('click', event => {
    event.preventDefault();
    updateSwal.fire({
        icon: 'question',
        title: '確定要更新商品資訊？',
        iconColor: '#3eb1e6',
        showClass: {
            popup: `
                    animate__animated
                    animate__fadeInUp
                    animate__faster
                    `
        },
        hideClass: {
            popup: `
                    animate__animated
                    animate__fadeOutDown
                    animate__faster
                    `
        }
    }).then(result => {
        if (result.isConfirmed) {
            updateAjax();
        }
    })
});

function updateAjax() {
    const productCategory = { 'categoryID': document.querySelector('#categoryID').value };
    const updateProduct = {
        'productID': document.querySelector('#productID').value,
        'productCategory': productCategory,
        'productName': document.querySelector('#productName').value,
        'price': document.querySelector('#price').value,
        'listingDate': document.querySelector('#listingDate').value,
        'stock': document.querySelector('#stock').value,
        'productFeatures': document.querySelector('#feature').value
    }

    axios.post('update', JSON.stringify(updateProduct), {
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(res => {
            Swal.fire({
                icon: 'success',
                title: '商品資訊更新成功',
                timer: 2000,
                timerProgressBar: true,
                toast: true,
                showConfirmButton: false,
                position: 'top'
            })
        })
        .catch(err => {
            updateSwal.fire({
                icon: 'warning',
                title: '刪除失敗',
                text: error,
            })
        });
};