package com.fullcyle.admin.catalog.application.castmember.update;

import com.fullcyle.admin.catalog.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {
}
