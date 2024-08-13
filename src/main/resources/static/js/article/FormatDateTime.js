function formatDateTime() {
				const dates = document.querySelectorAll('.date');
				dates.forEach(date => {
					const fullDate = date.innerText;
					const cutPoint = fullDate.indexOf('.');
					if (cutPoint !== -1) {
						const formattedTime = fullDate.substring(0, cutPoint);
						date.innerText = formattedTime;
					}
				});
			}
	
			window.onload = function() {
				formatDateTime();
				}