function fillLocalTime() {
   				// 取得當前時間
    			const now = new Date();
    			// 將本地時間格式化為 yyyy-mm-dd HH:MM:SS 格式
    			const formattedTime = now.toISOString().slice(0, 19).replace('T', ' ');
    			// 將格式化後的時間填入發文時間欄位
    			document.getElementsByName("postTime")[0].value = formattedTime;
    			
			}
			