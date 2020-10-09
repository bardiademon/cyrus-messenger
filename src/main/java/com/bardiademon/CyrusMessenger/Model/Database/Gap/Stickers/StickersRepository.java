package com.bardiademon.CyrusMessenger.Model.Database.Gap.Stickers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickersRepository extends JpaRepository <Stickers, Long>
{
}
