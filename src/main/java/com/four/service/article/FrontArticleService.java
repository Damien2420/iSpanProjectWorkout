package com.four.service.article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.four.model.article.Article;
import com.four.model.article.ArticleRepository;
import com.four.model.memberAdm.MemberBean;

@Service
public class FrontArticleService {

	@Autowired
	private ArticleRepository articleRepo;

	//取的主要頁面的文章
	public Page<Article> findByPage(Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "articleId");
		Page<Article> page = articleRepo.findByArticleDisplay("1", pageable);
		return cutArticleIfTooLong(page);
	}

	//分類欄位顯示各分類數量
	public Map<String, Integer> getTagCount() {
		String question = "問題";
		String discussion = "討論";
		String opinion = "心得";
		String diet = "飲食";
		String other = "其他";
		List<String> tag = new ArrayList<>();
		tag.add(question);
		tag.add(discussion);
		tag.add(opinion);
		tag.add(diet);
		tag.add(other);
		System.out.println("size="+tag.size());
		Map<String, Integer> tagCount = new HashMap<>();

		for(int i=0; i< tag.size();i++) {
			Integer count = articleRepo.getTagCount(tag.get(i));
			tagCount.put(tag.get(i), count);
		}
		System.out.println("tagcount="+ tagCount);
		return tagCount;
	}
	
	//取得側邊熱門文章 利用留言數排序 且取前五
	public List<Article> getSideArticles(){
		List<Article> result = articleRepo.findArticlesByDisplayOrderByCommentCountDesc("1");
		List<Article> articles = new ArrayList<>();
		for(int i=0; i<5; i++) {
			articles.add(result.get(i));
			System.out.println("第"+i+"項"+articles.get(i).getArticleTitle());
		}
		return articles;
	}
	
	//點下問題分類欄內的分類
	public Page<Article> getArticleByTag(String tag){
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "articleId");
		Page<Article> page = articleRepo.findByTagAndArticleDisplay(tag, "1", pageable);
		return cutArticleIfTooLong(page);
		
	}
	
	//用文章id查單一文章 掃過整個字串 看有沒有imgur連結
	public Article getArticleById(Integer articleId) {
		Optional<Article> optional = articleRepo.findById(articleId);
		if(optional.isEmpty()) {
			return null;
		}
		replaceLinkWithImage(optional.get().getArticleContent());
		return optional.get();
	}
	
	//把剪裁文章的邏輯獨立成function
	private Page<Article> cutArticleIfTooLong(Page<Article> page) {
		List<Article> articles = page.getContent();
		for (Article article : articles) {

			Integer lessWordStatus = 0;
			Integer displayWordCounts = 150;// 展示總內文字數為150字
			String content = article.getArticleContent();
			if (content.length() < displayWordCounts) {
				displayWordCounts = content.length();
				lessWordStatus = 1;
			}
			//剪裁避免文章過長
			String cutContent = content.substring(0, displayWordCounts);
			if (lessWordStatus == 0) {// 總字數低於150字就加上"..."
				String dotContent = cutContent.concat("...");
				article.setArticleContent(dotContent);
			} else {
				article.setArticleContent(cutContent);
			}
		}
		return page;
	}
	
	//新增一筆文章
	public void insertArticle(String articleContent,
								String category,
								String articleTitle,
								String postTime,
								MemberBean member) {
		Article article = new Article();
		article.setArticleContent(articleContent);
		article.setTag(category);
		article.setArticleTitle(articleTitle);
		//如果session沒抓到會員
		if(member != null) {
			article.setMemberId(member);
		}else {
			MemberBean memberTemp = new MemberBean();
			memberTemp.setMemNo(10000001);
			article.setMemberId(memberTemp);
		}
		article.setPostTime(postTime);
		article.setArticleDisplay("1");
		article.setCommentCount(0);
		article.setLikeAmount(0);
		articleRepo.save(article);
	}
	
	//查前一篇文章
	public Article getPreviousArticleById(Integer articleId) {
		if(articleId != 1) {
			articleId = articleId - 1;
			Optional<Article> optional = articleRepo.findById(articleId);
			if(optional.isEmpty()) {
				return null;
			}
			return optional.get();
		}
		return null;
	}
	
	//查後一篇文章
	public Article getNextArticleById(Integer articleId) {
		Integer articleAmount = articleRepo.getArticleAmount();
		if(articleId != articleAmount) {
			articleId = articleId + 1;
			Optional<Article> optional = articleRepo.findById(articleId);
			if(optional.isEmpty()) {
				return null;
			}
			return optional.get();
		}
		return null;
	}
	
	//查詢文章總數
	public Integer getArticleAmount() {
		return articleRepo.getArticleAmount();
	}
	
	//取得特定會員的所有文章 (前台 會員中心)
	public List<Article> getArticlesByMemberId(MemberBean memberId){
		return articleRepo.findByMemberIdAndArticleDisplay(memberId, "1");
	}
	
	//用articleId查一筆
	public Article findByArticleId(Integer articleId) {
		Optional<Article> optional = articleRepo.findById(articleId);
		if(optional.isEmpty()) {
			return null;
		}
		return optional.get();
	}
	
	//存一筆文章
	public void saveArticle(Article article) {
		articleRepo.save(article);
	}
	
	//把圖片連結轉成圖片 //查imgur 如果有 往前抓看看http 如果有 往後抓網址
	private String replaceLinkWithImage(String articleContent) {
		String imgurUrl = "https://imgur.com/";
		Integer imgurUrlAmount = 18;
		
		List<Integer> positions = findAllOccurrences(articleContent, imgurUrl);
		System.out.println("positions位置在 = "+ positions);
		ArrayList<String> urls = new ArrayList<>();
		Integer urlAmount = positions.size();
		for(int i = 0; i < urlAmount; i++) {
			Integer startPosition = positions.get(i) + imgurUrlAmount;
			Integer endPostion = startPosition + 7;
			String url = articleContent.substring(startPosition, endPostion);
			System.out.println("URL= " + url);
			urls.add(url);
		}
		
		return null;
	}
	
	private static List<Integer> findAllOccurrences(String articleContent, String imgurUrl) {
        List<Integer> positions = new ArrayList<>();
        int index = 0;
        
        while ((index = articleContent.indexOf(imgurUrl, index)) != -1) {
            positions.add(index);
            index += imgurUrl.length();
        }
        
        return positions;
    }
	
	
	
	

}
