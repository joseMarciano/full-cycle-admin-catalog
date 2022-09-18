package com.fullcyle.admin.catalog.application.castmember.create;

import com.fullcyle.admin.catalog.application.UseCase;

public sealed abstract class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}
