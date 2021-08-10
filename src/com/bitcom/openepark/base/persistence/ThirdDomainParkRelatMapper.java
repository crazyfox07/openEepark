package com.bitcom.openepark.base.persistence;

import com.bitcom.openepark.base.domain.ThirdDomainParkRelat;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ThirdDomainParkRelatMapper {
    int deleteByPrimaryKey(String paramString);

    int insert(ThirdDomainParkRelat paramThirdDomainParkRelat);

    int insertSelective(ThirdDomainParkRelat paramThirdDomainParkRelat);

    ThirdDomainParkRelat selectByPrimaryKey(String paramString);

    int updateByPrimaryKeySelective(ThirdDomainParkRelat paramThirdDomainParkRelat);

    int updateByPrimaryKey(ThirdDomainParkRelat paramThirdDomainParkRelat);

    @Delete({"delete from third_domain_park_relat where domain_id = #{domainId}"})
    void deleteByDomainId(@Param("domainId") String paramString);

    @Select({"select count(*) from third_domain_park_relat where park_code = #{parkCode} and domain_id <> #{domainId}"})
    int queryRecordCountExcept(@Param("domainId") String paramString1, @Param("parkCode") String paramString2);

    List<ThirdDomainParkRelat> selectByDomainId(@Param("domainId") String paramString);
}



