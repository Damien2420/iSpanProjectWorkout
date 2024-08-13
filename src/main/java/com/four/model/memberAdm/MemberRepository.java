package com.four.model.memberAdm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<MemberBean, Integer> {
	
	MemberBean findByMemEmail(String memEmail);
	
	// 取得每月註冊人數(月份, 人數)
	// 使用原生 SQL 查询时，Spring Data JPA 無法自動將查詢結果映射到自定義的數據類型（如 MemberRegistrationData）
	// 在service or controller中手動將查詢結果轉換為 MemberRegistrationData 對象
	@Query(value = "SELECT MONTH(regDate) AS month, COUNT(*) AS count " +
	            "FROM member " +
	            "GROUP BY MONTH(regDate) " +
	            "ORDER BY MONTH(regDate)", 
	    nativeQuery = true)
	List<Object[]> findMemberRegistrationData();
//	List<MemberRegistrationData> findMemberRegistrationData();
	
	// 取得會員年齡人數
//	@Query("SELECT new com.four.model.memberAdm.MemberAgeDTO(CASE WHEN age < 21 THEN '20以下' " +
//            "WHEN age BETWEEN 21 AND 30 THEN '21-30' " +
//            "WHEN age BETWEEN 31 AND 40 THEN '31-40' " +
//            "WHEN age BETWEEN 41 AND 50 THEN '41-50' " +
//            "WHEN age BETWEEN 51 AND 60 THEN '51-60' " +
//            "WHEN age BETWEEN 61 AND 70 THEN '61-70' " +
//            "ELSE '70以上' END, COUNT(m)) " +
//            "FROM Member m GROUP BY CASE WHEN age < 21 THEN '20以下' " +
//            "WHEN age BETWEEN 21 AND 30 THEN '21-30' " +
//            "WHEN age BETWEEN 31 AND 40 THEN '31-40' " +
//            "WHEN age BETWEEN 41 AND 50 THEN '41-50' " +
//            "WHEN age BETWEEN 51 AND 60 THEN '51-60' " +
//            "WHEN age BETWEEN 61 AND 70 THEN '61-70' " +
//            "ELSE '70以上' END")
//    List<MemberAgeDTO> findMemberAgeGroups();
	
	@Query(value = "SELECT " +
            "CASE " +
            "WHEN age < 21 THEN '20以下' " +
            "WHEN age BETWEEN 21 AND 30 THEN '21-30' " +
            "WHEN age BETWEEN 31 AND 40 THEN '31-40' " +
            "WHEN age BETWEEN 41 AND 50 THEN '41-50' " +
            "WHEN age BETWEEN 51 AND 60 THEN '51-60' " +
            "WHEN age BETWEEN 61 AND 70 THEN '61-70' " +
            "ELSE '70以上' END AS ageGroup, " +
            "COUNT(*) AS count " +
            "FROM member " +
            "GROUP BY " +
            "CASE " +
            "WHEN age < 21 THEN '20以下' " +
            "WHEN age BETWEEN 21 AND 30 THEN '21-30' " +
            "WHEN age BETWEEN 31 AND 40 THEN '31-40' " +
            "WHEN age BETWEEN 41 AND 50 THEN '41-50' " +
            "WHEN age BETWEEN 51 AND 60 THEN '51-60' " +
            "WHEN age BETWEEN 61 AND 70 THEN '61-70' " +
            "ELSE '70以上' END", nativeQuery = true)
//	List<MemberAgeDTO> findMemberAgeGroups();
	List<Object[]> findMemberAgeGroups();


}
