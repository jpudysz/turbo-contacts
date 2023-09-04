#import "TurboContacts.h"

@implementation TurboContacts

RCT_EXPORT_MODULE()

- (NSArray<NSDictionary *> *)getAllContacts {
    NSArray *contacts = [NSArray new];

    return contacts;
}

- (NSNumber *)hasContactsPermission {
    return @NO;
}

- (void)requestContactsPermission:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    resolve(@NO);
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
    return std::make_shared<facebook::react::NativeTurboContactsSpecJSI>(params);
}

@end
