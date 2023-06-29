package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class UserListEntryBan<T> extends UserListEntry<T>
{
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    protected final Date banStartDate;
    protected final String bannedBy;
    protected final Date banEndDate;
    protected final String reason;

    public UserListEntryBan(T valueIn, Date startDate, String banner, Date endDate, String banReason)
    {
        super(valueIn);
        banStartDate = startDate == null ? new Date() : startDate;
        bannedBy = banner == null ? "(Unknown)" : banner;
        banEndDate = endDate;
        reason = banReason == null ? "Banned by an operator." : banReason;
    }

    protected UserListEntryBan(T valueIn, JsonObject json)
    {
        super(valueIn, json);
        Date date;

        try
        {
            date = json.has("created") ? DATE_FORMAT.parse(json.get("created").getAsString()) : new Date();
        }
        catch (ParseException var7)
        {
            date = new Date();
        }

        banStartDate = date;
        bannedBy = json.has("source") ? json.get("source").getAsString() : "(Unknown)";
        Date date1;

        try
        {
            date1 = json.has("expires") ? DATE_FORMAT.parse(json.get("expires").getAsString()) : null;
        }
        catch (ParseException var6)
        {
            date1 = null;
        }

        banEndDate = date1;
        reason = json.has("reason") ? json.get("reason").getAsString() : "Banned by an operator.";
    }

    public Date getBanEndDate()
    {
        return banEndDate;
    }

    public String getBanReason()
    {
        return reason;
    }

    boolean hasBanExpired()
    {
        return banEndDate != null && banEndDate.before(new Date());
    }

    protected void onSerialization(JsonObject data)
    {
        data.addProperty("created", DATE_FORMAT.format(banStartDate));
        data.addProperty("source", bannedBy);
        data.addProperty("expires", banEndDate == null ? "forever" : DATE_FORMAT.format(banEndDate));
        data.addProperty("reason", reason);
    }
}
