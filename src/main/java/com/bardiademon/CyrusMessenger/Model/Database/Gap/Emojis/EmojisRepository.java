package com.bardiademon.CyrusMessenger.Model.Database.Gap.Emojis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmojisRepository extends JpaRepository <Emojis, Long>
{
}
