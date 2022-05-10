package com.sellertool.server.domain.inventory_release.repository;

import com.sellertool.server.domain.inventory_release.entity.InventoryReleaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryReleaseRepository extends JpaRepository<InventoryReleaseEntity, Long>, InventoryReleaseRepositoryCustom{
    
//    /**
//     * ProductRelease cid에 대응하는 출고데이터의 M2OJ 관계인(상품, 상품옵션, 카테고리, 출고, 유저) 데이터를 조회한다.
//     *
//     * @param cid : Integer
//     * @return InventoryReleaseProj
//     */
//    @Query(
//        "SELECT pl AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM InventoryReleaseEntity pl\n"+
//        "JOIN OptionEntity po ON po.cid=pl.optionCid\n"+
//        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
//        "JOIN CategoryEntity pc ON pc.cid=p.categoryCid\n"+
//        "JOIN UserEntity u ON u.id=pl.createdBy\n"+
//        "WHERE pl.cid=:cid"
//    )
//    Optional<InventoryReleaseProj> selectByCid(Integer cid);
//
//    /**
//     * ProductRelease 출고데이터의 M2OJ 관계인(상품, 상품옵션, 카테고리, 출고, 유저) 데이터를 모두 조회한다.
//     *
//     * @return ProductReleaseProj
//     */
//    @Query(
//        "SELECT pl AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM InventoryReleaseEntity pl\n"+
//        "JOIN ProductOptionEntity po ON po.cid=pl.productOptionCid\n"+
//        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
//        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
//        "JOIN UserEntity u ON u.id=pl.createdBy"
//    )
//    List<InventoryReleaseProj> selectAll();
//
//    @Query(
//        "SELECT pl AS productRelease, po AS productOption, p AS product, u AS user, pc AS category FROM InventoryReleaseEntity pl\n"+
//        "JOIN ProductOptionEntity po ON po.cid=pl.productOptionCid\n"+
//        "JOIN ProductEntity p ON p.cid=po.productCid\n"+
//        "JOIN ProductCategoryEntity pc ON pc.cid=p.productCategoryCid\n"+
//        "JOIN UserEntity u ON u.id=pl.createdBy\n"+
//        "WHERE pl.createdAt BETWEEN :startDate AND :endDate\n"+
//        "ORDER By pl.createdAt DESC"
//    )
//    List<InventoryReleaseProj> selectAll(LocalDateTime startDate, LocalDateTime endDate);
//
//    /**
//     * ProductRelease cid값에 대응하는 출고데이터를 조회한다.
//     *
//     * @param cids : List::Integer::
//     * @return List::InventoryReleaseEntity
//     */
//    @Query(
//        "SELECT pl\n" +
//        "FROM InventoryReleaseEntity pl\n" +
//        "WHERE pl.cid IN :cids"
//    )
//    List<InventoryReleaseEntity> selectAllByCid(List<Integer> cids);
//
//    /**
//     * productOption cid값에 대응하는 ProductRelease 출고데이터를 조회한다.
//     *
//     * @param productOptionCid : Integer
//     * @return List::InventoryReleaseEntity::
//     */
//    List<InventoryReleaseEntity> findByProductOptionCid(Integer productOptionCid);
//
//    @Transactional
//    @Modifying
//    @Query(
//            "DELETE FROM InventoryReleaseEntity rs\n" +
//            "WHERE rs.erpOrderItemId IN :ids"
//    )
//    void deleteByErpOrderItemIds(List<UUID> ids);
}
