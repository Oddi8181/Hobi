package gio.hobist.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor

public class HobbyUserId implements Serializable {

    private UUID user;

    private UUID hobby;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HobbyUserId that = (HobbyUserId) o;
        return Objects.equals(user, that.user)
                && Objects.equals(hobby, that.hobby);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, hobby);
    }
}
