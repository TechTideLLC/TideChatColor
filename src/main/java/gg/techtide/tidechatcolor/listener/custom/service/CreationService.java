package gg.techtide.tidechatcolor.listener.custom.service;

import gg.techtide.tidelib.patterns.service.impl.EclipseService;
import gg.techtide.tidelib.patterns.service.type.ServiceType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CreationService extends EclipseService<UUID>
{
    public CreationService() {
        super(ServiceType.SET);
    }
}
