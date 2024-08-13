const bodyRows = document.querySelectorAll('tbody tr');
const buttons = document.querySelector('.buttons');
const originalButtonContent = buttons.innerHTML;
const deleteContent = `
<div class="col d-flex justify-content-center align-items-center">
    <button class="functionBtn delete">
        <i class="fa-solid fa-xmark"></i> 取消刪除
    </button>
</div>
<div class="col d-flex justify-content-center align-items-center">
    <button class="functionBtn delete-confirmed">
        <i class="fa-solid fa-trash-can"></i> 確認刪除
    </button>
</div>`;

const afterSearchButtons = `
<div class="buttons d-flex justify-content-between flex-row">
    <div class="col d-flex justify-content-center align-items-center">
        <a href="allList">
            <button class="functionBtn main-menu">
                <i class="fa-solid fa-house"></i> 回商品列表
            </button>
        </a>
    </div>
    <div class="col d-flex justify-content-center align-items-center">
        <button class="functionBtn delete">
            <i class="fa-solid fa-trash-can"></i> 刪除資料
        </button>
    </div>
</div>`;

// 進入畫面執行
document.addEventListener('DOMContentLoaded', function () {
    toggleDelete();
    updateLaunchEvent();
});

const searchControl = document.querySelector('main').getAttribute('data-search-status') === "true" ? true : false;
console.log(searchControl);
const totalMsg = document.querySelector('.totalmsg');
if (document.querySelectorAll('tbody tr').length > 0 && searchControl) {
    const resultRows = document.querySelector('#data-row-count').innerText;
    if (!isNaN(resultRows)) {
        totalMsg.innerText = `總共有 ${resultRows} 筆商品資料`;
    }
} else if (!searchControl) {
    totalMsg.innerText = '';
} else {
    totalMsg.innerText = '沒有搜尋到匹配的商品';
};

// 監聽表格變化以顯示刪除按鈕
const observer = new MutationObserver(toggleDelete);
observer.observe(document.querySelector('tbody'), { childList: true });

document.querySelectorAll('.delete-column input[type="checkbox"]').forEach(function (checkbox) {
    checkbox.addEventListener('change', updateButtonState);
});

// 如果沒有勾選就disable確認按鈕
function updateButtonState() {
    let anyChecked = document.querySelectorAll('.delete-column input[type="checkbox"]:checked').length > 0;
    document.querySelector('.delete-confirmed').disabled = !anyChecked;
}

// 沒有資料就不顯示刪除按鈕
function toggleDelete() {
    let rowCount = bodyRows.length;
    if (rowCount === 0) {
        buttons.innerHTML = originalButtonContent;
    } else {
        buttons.innerHTML = afterSearchButtons;
        cancelDeleteEvent(document.querySelector('.delete'));
    }
}

// 取消勾選刪除checkbox
function uncheck() {
    const deleteCheckbox = document.querySelectorAll('input[name="delete"]:checked');
    deleteCheckbox.forEach(checkbox => {
        checkbox.checked = false;
    });
}

// 刪除列與功能按鈕控制
let isDeleteFuncActive = false;
function deleteRow() {
    if (!isDeleteFuncActive) {
        buttons.innerHTML = deleteContent;
        document.querySelectorAll('.delete-column').forEach(function (element) {
            element.style.display = 'table-cell';
        })
        const deleteButton = document.querySelector('.delete');
        cancelDeleteEvent(deleteButton);
        deleteButton.style.backgroundColor = 'lightgreen';
        const deleteConfirmButton = document.querySelector('.delete-confirmed');
        deleteConfirmButton.addEventListener('click', () => {
            deleteAlert();
        });
    } else {
        buttons.innerHTML = originalButtonContent;
        const deleteButton = document.querySelector('.delete');
        document.querySelectorAll('.delete-column').forEach(function (element) {
            element.style.display = 'none';
        })
    }
    isDeleteFuncActive = !isDeleteFuncActive;
}


// 觸發刪除列的監聽事件
function cancelDeleteEvent(button) {
    button.addEventListener('click', () => {
        deleteRow();
        uncheck();
    });
}

function updateLaunchEvent() {
    document.querySelectorAll('.launch-status input[type="checkbox"]').forEach(function (checkbox) {
        checkbox.addEventListener('change', () => {
            const launchStatus = Boolean(checkbox.checked);
            const row = checkbox.closest('tr');
            const productID = row.querySelector('td:first-child a').innerText.trim();
    
            axios.put(`updateLaunch/${productID}?status=${launchStatus}`)
            .then(res => {
                console.log(res);
            })
            .catch(err => {
                console.error(err); 
            })
        })
    });
};


document.getElementById('search-submit').addEventListener('click', function (event) {
    if (validateForm()) {
        document.querySelector('.search-form').submit();
    } else {
        event.preventDefault();
        Swal.fire({
            icon: 'warning',
            title: '請輸入關鍵字！',
            confirmButtonText: '確定'
        });
    }
});

function validateForm() {
    const searchInput = document.getElementById('search-input').value;
    return searchInput.trim() !== '';
}