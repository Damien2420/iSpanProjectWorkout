const features = document.querySelector('.features');
const ratings = document.querySelector('.ratings');
const login = JSON.parse(document.querySelector('.main-section').dataset.user);

const isReviewExist = JSON.parse(document.querySelector('#review').dataset.review);
if(isReviewExist){
	totalRating();
	userRating();
}



document.querySelectorAll('.gallery-img').forEach(function(image) {
	image.addEventListener('click', function() {
		const imageURL = image.src;
		document.querySelector('.img-container img').src = imageURL;
	});
});

document.querySelectorAll('.tab-nav h3').forEach(tab => {
	tab.addEventListener('click', function () {
		document.querySelector('.tab-nav h3.active').classList.remove('active');
		tab.classList.add('active');
		const tabWords = tab.innerText;
		if(tabWords.includes('特色')) {
			features.classList.remove('hide');
			ratings.classList.add('hide');
		}else {
			ratings.classList.remove('hide');
			features.classList.add('hide');
		}
	});
});

function totalRating() {
	const totalRating = document.querySelector('.total-rating .rating-num').innerText.substring(0, 1);
	const ratingStars = document.querySelectorAll('.total-rating .rating span');
	for(let i = 0;i < totalRating;i++) {
		ratingStars[i].classList.add('checked');
	};
};

function userRating() {
	const allRatings = document.querySelectorAll('.user-rating');
	allRatings.forEach(ratingEle => {
		const rating = ratingEle.querySelector('.rating-num').innerText.substring(0, 1);
		const ratingStars = ratingEle.querySelectorAll('.rating span');
		for(let i = 0;i < rating;i++) {
			ratingStars[i].classList.add('checked');
		};
	})
};

document.querySelector('#purchase').addEventListener('click', function () {
	checkLogin(0);
});

document.querySelector('#cart').addEventListener('click', function () {
	checkLogin(1);
});

function checkLogin(action) {
	if(!login) {
		Swal.fire({
			title: '請先登入！',
			confirmButtonText: "確認",
			confirmButtonColor: '#f36100'
		}).then(result => {
			if(result.isConfirmed) {
				window.location.href = '../loginregister.controller';
			}
		});
	}else {
		purchase(action);
	};
}

async function purchase(action) {
	let quantity = document.querySelector('.number');
	if (quantity.value != 0) {
		try {
			const response = await addToCart();
			if (response.status === 200) {
				switch (action) {
					case 0:
						window.location.href = 'cart';
						break;
					case 1:
						Swal.fire({
							icon: 'success',
							title: '加入購物車成功',
							timer: 2000,
							timerProgressBar: true,
							toast: true,
							showConfirmButton: false,
							position: 'top'
						})
						quantity.value = 0;
						break;
				}
			}
		} catch (error) {
			Swal.fire({
				icon: 'error',
				title: '請求失敗，請重試',
				text: error.message
			});
		}
	} else {
		Swal.fire({
			icon: 'warning',
			title: '請先選擇商品數量!'
		});
	}
}

async function addToCart() {
	const productID = document.querySelector('#id').value;
	const quantity = document.querySelector('.number').value;
	try {
		const response = await axios.post('addToCart', {
			productID: productID,
			quantity: quantity
		});
		document.querySelector('.cart span').innerText = `(${response.data})`;
		return response;
	} catch (error) {
		throw new Error(error);
	}
}
