package com.fastcampus.pass.repository.packaze;

import com.fastcampus.pass.config.TestBatchConfig;
import com.fastcampus.pass.job.pass.ExpirePassesJobConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {ExpirePassesJobConfig.class, TestBatchConfig.class})
class PackageRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @DisplayName("save test")
    @Test
    public void test_save(){
        //  given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디 챌린지 PT 12주");
        packageEntity.setPeriod(84);

        //  when
        packageRepository.save(packageEntity);

        //  then
        assertThat(packageEntity).isNotNull();

    }

    @Test
    void test_findByCreatedAtAfter() {
        //  given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("학생 전용 3개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("학생 전용 6개월");
        packageEntity1.setPeriod(180);
        packageRepository.save(packageEntity1);

        //  when
        final List<PackageEntity> packageEntities = packageRepository
                .findByCreatedAtAfter(dateTime, PageRequest.of(
                        0, 1, Sort.by("packageSeq").descending())
                );

        //  then
        assertThat(packageEntities).hasSize(1);
        assertThat(packageEntities.get(0))
                .hasFieldOrPropertyWithValue("packageName", "학생 전용 6개월")
                .hasFieldOrPropertyWithValue("period", 180);
    }

    @Test
    public void test_updateCountAndPeriod() {
        //  given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디프로필 이벤트 4개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        //  when
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);

        final PackageEntity updatedPackageEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        //  then
        assertThat(updatedCount).isEqualTo(1);
        assertThat(updatedPackageEntity.getCount()).isEqualTo(30);
        assertThat(updatedPackageEntity.getPeriod()).isEqualTo(120);
    }

    @Test
    public void test_delete() {
        //  given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("제거할 이용권");
        packageEntity.setCount(1);
        PackageEntity newPackageEntity = packageRepository.save(packageEntity);

        //  when
        packageRepository.deleteById(newPackageEntity.getPackageSeq());

        //  then
        assertThat(packageRepository.findById(newPackageEntity.getPackageSeq())).isEmpty();

    }

}