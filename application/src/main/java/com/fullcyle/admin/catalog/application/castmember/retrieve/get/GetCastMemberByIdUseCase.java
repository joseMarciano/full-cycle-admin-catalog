package com.fullcyle.admin.catalog.application.castmember.retrieve.get;

import com.fullcyle.admin.catalog.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
