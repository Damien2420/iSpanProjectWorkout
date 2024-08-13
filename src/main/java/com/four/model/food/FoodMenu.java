package com.four.model.food;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.four.model.memberAdm.MemberBean;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor 
@Setter
@Getter
@Entity
@Table(name = "foodMenu")
public class FoodMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer foodMenuID;//菜單編號

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memNo", nullable = false,referencedColumnName = "memNo")
    private MemberBean member ;//會員編號
    
//    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "foodMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodMenuDetail> foodMenuDetails;

    private float totalCal;//總熱量
    
    @Temporal(TemporalType.TIMESTAMP)//儲存日期&時間
    @Column(name = "createMenuTime", nullable = false, updatable = false)//不得為空值&不可更新
    private Date createTime;//建立時間
    
    
    @PrePersist //在保存之前自動建立時間
    protected void onCreate() {
        createTime = new Date();
    }
    
    public void addFoodDetail(FoodMenuDetail detail) {
    	foodMenuDetails.add(detail);
        detail.setFoodMenu(this);
    }

    public void removeFoodDetail(FoodMenuDetail detail) {
    	foodMenuDetails.remove(detail);
        detail.setFoodMenu(null);
    }
    
}
