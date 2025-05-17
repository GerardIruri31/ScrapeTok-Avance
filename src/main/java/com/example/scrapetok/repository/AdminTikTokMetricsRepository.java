package com.example.scrapetok.repository;

import com.example.scrapetok.domain.AdminTiktokMetrics;
import com.example.scrapetok.domain.DTO.HashtagTrendDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AdminTikTokMetricsRepository extends JpaRepository<AdminTiktokMetrics,Long> {
    @Query(value = """
    SELECT username_tiktok_account AS username, SUM(views) AS total_views
    FROM user_tiktok_metrics
    WHERE date_tracking = CURRENT_DATE
    GROUP BY username_tiktok_account
    """, nativeQuery = true)
    List<Map<String, Object>> findViewsGroupedByUsernameForToday();


    @Query(value = """
    SELECT sound_id AS username, SUM(views) AS total_views
    FROM user_tiktok_metrics
    WHERE date_tracking = CURRENT_DATE
    GROUP BY sound_id
    """, nativeQuery = true)
    List<Map<String, Object>> findViewsGroupedByMusicIdForToday();


    @Query(value = """
    SELECT date_posted, SUM(views) AS total_views
    FROM user_tiktok_metrics
    WHERE date_tracking = CURRENT_DATE
    GROUP BY date_posted
    ORDER BY date_posted
    """, nativeQuery = true)
    List<Map<String, Object>> findViewsGroupedByPostDateTrackedToday();

    @Query(value = """
    SELECT 
        TRIM(unnest(string_to_array(hashtags, ','))) AS hashtag,
        COUNT(*) AS frecuencia,
        ROUND(AVG(views), 2) AS promedio_views
    FROM user_tiktok_metrics
    WHERE date_tracking = CURRENT_DATE AND hashtags IS NOT NULL
    GROUP BY hashtag
    ORDER BY frecuencia DESC
    LIMIT 15
    """, nativeQuery = true)
    List<HashtagTrendDTO> findTopHashtagsTrendingToday();
}
