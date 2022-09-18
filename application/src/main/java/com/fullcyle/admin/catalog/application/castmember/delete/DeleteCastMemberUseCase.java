package com.fullcyle.admin.catalog.application.castmember.delete;

import com.fullcyle.admin.catalog.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase {
}
