//
//  String+Localized.swift
//  wallet-ios-core
//
//  Created by 仇弘扬 on 2018/1/5.
//  Copyright © 2018年 BitBill. All rights reserved.
//

import Foundation

extension String {
    static var publicCurrencyCNYName: String { get { return "Public.currency.name.cny".bil_localized } }
    static var publicCurrencyUSDName: String { get { return "Public.currency.name.usd".bil_localized } }
    static var publicKeyboardHide: String { get { return "Public.keyboard.hide".bil_localized } }
    static var publicNetworkConnectServerFaild: String { get { return "Public.network.connect.sever.failed".bil_localized } }
    static var publicWalletFetchError: String { get { return "Public.wallet.fetch.failed".bil_localized } }
    static var publicWalletExtKeyError: String { get { return "Public.wallet.error.extKey".bil_localized } }
    static var publicWalletIDError: String { get { return "Public.wallet.error.id".bil_localized } }
    static var publicWalletDataError: String { get { return "Public.wallet.error.data".bil_localized } }
    static var publicWalletGetAddressError: String { get { return "Public.wallet.error.address".bil_localized } }
    static var publicWalletNotRefresh: String { get { return "Public.wallet.error.address.dont.need.refresh".bil_localized } }
    static var publicWalletNoMoreAddress: String { get { return "Public.wallet.error.address.nomore".bil_localized } }
    static var publicWalletGenerateAddressError: String { get { return "Public.wallet.error.address.generate".bil_localized } }
    static var publicWalletIndexError: String { get { return "Public.wallet.error.address.index".bil_localized } }
    static var publicTransactionUnconfirm: String { get { return "Public.transaction.status.unconfirm".bil_localized } }
    static var publicTransactionSuccess: String { get { return "Public.transaction.status.success".bil_localized } }
    static var publicTransactionFailure: String { get { return "Public.transaction.status.failure".bil_localized } }
    static var publicTransactionConfirmed: String { get { return "Public.transaction.status.confirmed".bil_localized } }
    static var publicTransactionTypeReceive: String { get { return "Public.transaction.type.receive".bil_localized } }
    static var publicTransactionTypeSend: String { get { return "Public.transaction.type.send".bil_localized } }
    static var publicTransactionTypeTransfer: String { get { return "Public.transaction.type.transfer".bil_localized } }
    static var publicTransactionNoRemark: String { get { return "Public.transaction.remark.none".bil_localized } }
    static var publicAlertActionTitle: String { get { return "Public.alert.action.title".bil_localized } }
    static var publicToastComingSoon: String { get { return "Public.toast.comingSoon".bil_localized } }
	
    static var homeHomeSectionShortcut: String { get { return "HomeC.home.section.shortcut".bil_localized } }
    static var homeHomeSectionAsset: String { get { return "HomeC.home.section.asset".bil_localized } }
    static var homeHomeSectionUnconfirm: String { get { return "HomeC.home.section.unconfirm".bil_localized } }
	static var homeHomeSectionWallet: String { get { return "HomeC.home.section.wallet".bil_localized } }
	static var homeHomeSectionSubTitleShortcut: String { get { return "HomeC.home.section.subtitle.shortcut".bil_localized } }
	static var homeHomeSectionSubTitleAsset: String { get { return "HomeC.home.section.subtitle.asset".bil_localized } }
	static var homeHomeSectionSubTitleUnconfirm: String { get { return "HomeC.home.section.subtitle.unconfirm".bil_localized } }
	static var homeHomeSectionSubTitleWallet: String { get { return "HomeC.home.section.subtitle.wallet".bil_localized } }
	static var homeHomeSectionUnitGe: String { get { return "HomeC.home.section.subtitle.unit.ge".bil_localized } }
	static var homeHomePopWalletNew: String { get { return "HomeC.home.pop.wallet.new".bil_localized } }
	static var homeHomePopWalletImport: String { get { return "HomeC.home.pop.wallet.import".bil_localized } }
	static var homeTxDetailHash: String { get { return "HomeC.tx.detail.cell.hash".bil_localized } }
	static var homeTxDetailReceiveAddress: String { get { return "HomeC.tx.detail.cell.address.receive".bil_localized } }
	static var homeTxDetailSendAddress: String { get { return "HomeC.tx.detail.cell.address.send".bil_localized } }
	static var homeTxDetailConfirm: String { get { return "HomeC.tx.detail.cell.confirm".bil_localized } }
	static var homeTxDetailRemark: String { get { return "HomeC.tx.detail.cell.remark".bil_localized } }
    static var homeTxDetailFee: String { get { return "HomeC.tx.detail.cell.fee".bil_localized } }
	static var homeTxDetailDate: String { get { return "HomeC.tx.detail.cell.date".bil_localized } }
	static var homeTxDetailNoTx: String { get { return "HomeC.tx.detail.noTx".bil_localized } }
    static var homeTxDetailTx: String { get { return "HomeC.tx.detail.tx".bil_localized } }
    static var homeTxDetailBlockchain: String { get { return "HomeC.tx.detail.blockchain".bil_localized } }
	
	static var receiveAddressTipTitle: String { get { return "ReceiveC.receive.address.tip.title".bil_localized } }
	static var receiveAddressTipMessage: String { get { return "ReceiveC.receive.address.tip.msg".bil_localized } }
	static var receiveAddressTipRefresh: String { get { return "ReceiveC.receive.address.tip.refresh".bil_localized } }
    static var receiveSpecificButtonTitle: String { get { return "ReceiveC.receive.specificButtonTitle".bil_localized } }
	static var receiveSpecificCheckAmountTipTitle: String { get { return "ReceiveC.specific.check.amount.tip.title".bil_localized } }
	static var receiveSpecificCheckAmountTipMessageEmpty: String { get { return "ReceiveC.specific.check.amount.tip.msg.empty".bil_localized } }
	static var receiveSpecificCheckAmountTipMessageZero: String { get { return "ReceiveC.specific.check.amount.tip.msg.zero".bil_localized } }
	
	static var sendSendAddressInvalid: String { get { return "SendC.send.address.invalid".bil_localized } }
	static var sendSendAddressEmpty: String { get { return "SendC.send.address.empty".bil_localized } }
	static var sendSendAddressInvalidTitle: String { get { return "SendC.send.address.invalid.title".bil_localized } }
	static var sendSendAddressError: String { get { return "SendC.send.address.error".bil_localized } }
	static var sendAmountCheckTipTitle: String { get { return "SendC.amount.check.amount.tip.title".bil_localized } }
	static var sendAmountCheckTipMessageEmpty: String { get { return "SendC.amount.check.amount.tip.msg.empty".bil_localized } }
	static var sendAmountCheckTipMessageZero: String { get { return "SendC.amount.check.amount.tip.msg.zero".bil_localized } }
	static var sendAmountCheckTipMessageNotEnough: String { get { return "SendC.amount.check.amount.tip.msg.notEnough".bil_localized } }
	static var sendConfirmMinute: String { get { return "SendC.confirm.minute".bil_localized } }
	static var sendConfirmHour: String { get { return "SendC.confirm.hour".bil_localized } }
	static var sendConfirmDay: String { get { return "SendC.confirm.day".bil_localized } }
	static var sendConfirmAverageTime: String { get { return "SendC.confirm.averageTime".bil_localized } }
	static var sendConfirmNeedSpend: String { get { return "SendC.confirm.needSpend".bil_localized } }
	static var sendConfirmNotEnoughBalace: String { get { return "SendC.confirm.notEnoughBalace".bil_localized } }
	static var sendConfirmCannotFindChangeAddress: String { get { return "SendC.confirm.cannotFindChangeAddress".bil_localized } }
	static var sendConfirmRemarkTooLong: String { get { return "SendC.confirm.remarkTooLong".bil_localized } }
	static var sendConfirmSendFailed: String { get { return "SendC.confirm.sendFailed".bil_localized } }
	static var sendConfirmPasswordTitle: String { get { return "SendC.confirm.passwordTitle".bil_localized } }
	static var sendConfirmPasswordMessageEmpty: String { get { return "SendC.confirm.passwordMessageEmpty".bil_localized } }
	static var sendConfirmPasswordMessageWrong: String { get { return "SendC.confirm.passwordMessageWrong".bil_localized } }
	static var sendConfirmCancel: String { get { return "SendC.confirm.cancel".bil_localized } }
	static var sendConfirmConfirm: String { get { return "SendC.confirm.confirm".bil_localized } }
	static var sendConfirmPasswordPlaceholder: String { get { return "SendC.confirm.passwordPlaceholder".bil_localized } }
	static var sendConfirmBuildTxFailed: String { get { return "SendC.confirm.buildTxFailed".bil_localized } }
	static var sendConfirmSeedFailed: String { get { return "SendC.confirm.seedFailed".bil_localized } }
	static var sendConfirmTryLater: String { get { return "SendC.confirm.tryLater".bil_localized } }
	static var sendResultInput: String { get { return "SendC.result.input".bil_localized } }
	static var sendResultInputName: String { get { return "SendC.result.inputName".bil_localized } }
	static var sendResultInputNamePlaceHolder: String { get { return "SendC.result.inputNamePlaceholder".bil_localized } }
	static var sendResultAddSuccess: String { get { return "SendC.result.addSuccess".bil_localized } }
}

extension String {
	static var meMe_agreementName: String { get { return "MeC.agreementName".bil_localized } }
	static var meMe_meSection_preference: String { get { return "MeC.meSection_preference".bil_localized } }
	static var meMe_meSection_contacts: String { get { return "MeC.meSection_contacts".bil_localized } }
	static var meMe_meSection_wallet: String { get { return "MeC.meSection_wallet".bil_localized } }
	static var meMe_meSection_system: String { get { return "MeC.meSection_system".bil_localized } }
	static var meMe_meSection_other: String { get { return "MeC.meSection_other".bil_localized } }
	static var meMe_meCell_shortcut: String { get { return "MeC.meCell_shortcut".bil_localized } }
	static var meMe_meCell_contactBackup: String { get { return "MeC.meCell_contactBackup".bil_localized } }
	static var meMe_meCell_contactRecover: String { get { return "MeC.meCell_contactRecover".bil_localized } }
	static var meMe_meCell_system: String { get { return "MeC.meCell_system".bil_localized } }
	static var meMe_meCell_aboutUs: String { get { return "MeC.meCell_aboutUs".bil_localized } }
	static var meMe_contact_yourKey: String { get { return "MeC.contact_yourKey".bil_localized } }
	static var meMe_contact_copyKey: String { get { return "MeC.contact_copyKey".bil_localized } }
	static var meMe_contact_keyCopied: String { get { return "MeC.contact_keyCopied".bil_localized } }
	static var meMe_contact_authFailed: String { get { return "MeC.contact_authFailed".bil_localized } }
	static var meMe_contact_inputKey: String { get { return "MeC.contact_inputKey".bil_localized } }
	static var meMe_contact_keyEmpty: String { get { return "MeC.contact_keyEmpty".bil_localized } }
	static var meMe_contact_noMore: String { get { return "MeC.contact_noMore".bil_localized } }
	static var meMe_contact_recovered: String { get { return "MeC.contact_recovered".bil_localized } }
	static var meMe_contact_contact: String { get { return "MeC.contact_contact".bil_localized } }
	static var meMe_confirm: String { get { return "MeC.confirm".bil_localized } }
	static var meMe_cancel: String { get { return "MeC.cancel".bil_localized } }
	
	static var meWallet_deleteFailed: String { get { return "MeC.wallet_deleteFailed".bil_localized } }
	static var meWallet_delete: String { get { return "MeC.wallet_delete".bil_localized } }
	static var meWallet_deleteMessage: String { get { return "MeC.wallet_deleteMessage".bil_localized } }
	static var meWallet_passwordEmpty: String { get { return "MeC.wallet_passwordEmpty".bil_localized } }
	static var meWallet_passwordError: String { get { return "MeC.wallet_passwordError".bil_localized } }
	static var meWallet_inputPassword: String { get { return "MeC.wallet_inputPassword".bil_localized } }
	static var meWallet_tryLater: String { get { return "MeC.wallet_tryLater".bil_localized } }
	static var meWallet_failed: String { get { return "MeC.wallet_failed".bil_localized } }
	static var meWallet_backupNow: String { get { return "MeC.wallet_backupNow".bil_localized } }
	
	static var meWalletAddress_addressAndBalance: String { get { return "MeC.walletAddress_addressAndBalance".bil_localized } }
	
	static var meAboutUs_agreement: String { get { return "MeC.aboutUs_agreement".bil_localized } }
    static var meAboutUs_contactUs: String { get { return "MeC.aboutUs_contactUs".bil_localized } }
	
	static var meAppSetting_sound: String { get { return "MeC.appSetting_sound".bil_localized } }
	static var meAppSetting_currency: String { get { return "MeC.appSetting_currency".bil_localized } }
    static var meAppSetting_language: String { get { return "MeC.appSetting_language".bil_localized } }
	static var meAppSetting_cellSound: String { get { return "MeC.appSetting_cellSound".bil_localized } }
	static var meAppSetting_cellCurrency: String { get { return "MeC.appSetting_cellCurrency".bil_localized } }
    static var meAppSetting_cellLanguage: String { get { return "MeC.appSetting_cellLanguage".bil_localized } }
	static var meAppSetting_saved: String { get { return "MeC.appSetting_saved".bil_localized } }
}

extension String {
	static var newWallet_inputID_IKnow: String { get { return "NewWalletC.inputID_IKnow".bil_localized } }
	static var newWallet_inputID_title: String { get { return "NewWalletC.inputID_title".bil_localized } }
	static var newWallet_inputID_range: String { get { return "NewWalletC.inputID_range".bil_localized } }
	static var newWallet_inputID_format: String { get { return "NewWalletC.inputID_format".bil_localized } }
	static var newWallet_inputID_prefix: String { get { return "NewWalletC.inputID_prefix".bil_localized } }
	static var newWallet_inputID_exits: String { get { return "NewWalletC.inputID_exits".bil_localized } }
	
	static var newWallet_create_typeNew: String { get { return "NewWalletC.create_typeNew".bil_localized } }
	static var newWallet_create_typeRecover: String { get { return "NewWalletC.create_typeRecover".bil_localized } }
	static var newWallet_create_typeResetPassword: String { get { return "NewWalletC.create_typeResetPassword".bil_localized } }
	static var newWallet_create_wallet: String { get { return "NewWalletC.create_wallet".bil_localized } }
	static var newWallet_create_begin: String { get { return "NewWalletC.create_begin".bil_localized } }
	static var newWallet_create_pwdInput: String { get { return "NewWalletC.create_pwdInput".bil_localized } }
	static var newWallet_create_pwdRange: String { get { return "NewWalletC.create_pwdRange".bil_localized } }
	static var newWallet_create_pwdNotEqual: String { get { return "NewWalletC.create_pwdNotEqual".bil_localized } }
	static var newWallet_create_failed: String { get { return "NewWalletC.create_failed".bil_localized } }
	static var newWallet_create_pwdEmpty: String { get { return "NewWalletC.create_pwdEmpty".bil_localized } }
	static var newWallet_create_createPwd: String { get { return "NewWalletC.create_createPwd".bil_localized } }
	static var newWallet_create_confirmPwd: String { get { return "NewWalletC.create_confirmPwd".bil_localized } }
	static var newWallet_createSuccess_new: String { get { return "NewWalletC.createSuccess_new".bil_localized } }
	static var newWallet_createSuccess_recover: String { get { return "NewWalletC.createSuccess_recover".bil_localized } }
	static var newWallet_createSuccess_reset: String { get { return "NewWalletC.createSuccess_reset".bil_localized } }
	static var newWallet_createSuccess: String { get { return "NewWalletC.createSuccess".bil_localized } }
    static var newWallet_create_agreementTitle: String { get { return "NewWalletC.create_agreementTitle".bil_localized } }
	static var newWallet_import_emptyTitle: String { get { return "NewWalletC.import_emptyTitle".bil_localized } }
	static var newWallet_import_failed: String { get { return "NewWalletC.import_failed".bil_localized } }
	static var newWallet_import_checkAgain: String { get { return "NewWalletC.import_checkAgain".bil_localized } }
	static var newWallet_import_exits: String { get { return "NewWalletC.import_exits".bil_localized } }
	static var newWallet_import_resetOrNot: String { get { return "NewWalletC.import_resetOrNot".bil_localized } }
	static var newWallet_import_reset: String { get { return "NewWalletC.import_reset".bil_localized } }
    static var newWallet_import_spaceTip: String { get { return "NewWalletC.import_spaceTip".bil_localized } }
    static var newWallet_import_12WordsTip: String { get { return "NewWalletC.import_12WordsTip".bil_localized } }
}

extension String {
	static var backupWallet_mnemonic_emptyTitle: String { get { return "BackupWalletC.mnmonic_emptyTitle".bil_localized } }
	static var backupWallet_mnemonic_snapshotTip: String { get { return "BackupWalletC.mnmonic_snapshotTip".bil_localized } }
	static var backupWallet_mnemonic_tryLater: String { get { return "BackupWalletC.mnmonic_tryLater".bil_localized } }
	static var backupWallet_mnemonic_failed: String { get { return "BackupWalletC.mnmonic_failed".bil_localized } }
	static var backupWallet_mnemonic_backupWallet: String { get { return "BackupWalletC.mnmonic_backupWallet".bil_localized } }
	static var backupWallet_mnemonic_inputPwd: String { get { return "BackupWalletC.mnmonic_inputPwd".bil_localized } }
	static var backupWallet_mnemonic_getWalletFailed: String { get { return "BackupWalletC.mnmonic_getWalletFailed".bil_localized } }
	static var backupWallet_mnemonic_decodeWalletFailed: String { get { return "BackupWalletC.mnmonic_decodeWalletFailed".bil_localized } }
	static var backupWallet_mnemonic_confirm: String { get { return "BackupWalletC.mnmonic_confirm".bil_localized } }
	static var backupWallet_mnemonic_cancel: String { get { return "BackupWalletC.mnmonic_cancel".bil_localized } }
	
	static var backupWallet_verify_emptyTitle: String { get { return "BackupWalletC.verify_emptyTitle".bil_localized } }
}

extension String {
	static var contact_contact_addByID: String { get { return "ContactC.contact_addByID".bil_localized } }
	static var contact_contact_addByAddress: String { get { return "ContactC.contact_addByAddress".bil_localized } }
	static var contact_contact_scanCode: String { get { return "ContactC.contact_scanCode".bil_localized } }
	static var contact_contact_cancel: String { get { return "ContactC.contact_cancel".bil_localized } }
	static var contact_contact_addressExits: String { get { return "ContactC.contact_addressExits".bil_localized } }
	static var contact_contact_addressInvalid: String { get { return "ContactC.contact_addressInvalid".bil_localized } }
	static var contact_contact_IDExits: String { get { return "ContactC.contact_IDExits".bil_localized } }
	
	static var contact_detail_walletID: String { get { return "ContactC.detail_walletID".bil_localized } }
	static var contact_detail_walletAddress: String { get { return "ContactC.detail_walletAddress".bil_localized } }
	static var contact_detail_id: String { get { return "ContactC.detail_id".bil_localized } }
	static var contact_detail_address: String { get { return "ContactC.detail_address".bil_localized } }
	static var contact_detail_error: String { get { return "ContactC.detail_error".bil_localized } }
    static var contact_detail_copied: String { get { return "ContactC.detail_copied".bil_localized } }
	
	static var contact_search_empty: String { get { return "ContactC.search_empty".bil_localized } }
	static var contact_search_tooLong: String { get { return "ContactC.search_tooLong".bil_localized } }
	
	static var contact_searchResult_name: String { get { return "ContactC.searchResult_name".bil_localized } }
	static var contact_searchResult_remark: String { get { return "ContactC.searchResult_remark".bil_localized } }
	static var contact_searchResult_input: String { get { return "ContactC.searchResult_input".bil_localized } }
	static var contact_searchResult_surport: String { get { return "ContactC.searchResult_surport".bil_localized } }
	static var contact_searchResult_wei: String { get { return "ContactC.searchResult_wei".bil_localized } }
	static var contact_searchResult_nameEmpty: String { get { return "ContactC.searchResult_nameEmpty".bil_localized } }
	static var contact_searchResult_success: String { get { return "ContactC.searchResult_success".bil_localized } }
}

extension Bundle {
	static var currentLanguageBundle: Bundle {
		get {
			guard let path = Bundle.main.path(forResource: BILSettingManager.currentLanguage.rawValue, ofType: "lproj") else { return .main }
			guard let bundle = Bundle(path: path) else {
				return .main
			}
			return bundle
		}
	}
}

extension String {
    var bil_localized: String {
        get {
            guard let tableName = components(separatedBy: ".").first else {
                return self
            }
            return Bundle.currentLanguageBundle.localizedString(forKey: self, value: nil, table: tableName)
        }
    }
    
    var bil_ui_localized: String {
        get {
             return Bundle.currentLanguageBundle.localizedString(forKey: self, value: nil, table: "UI")
        }
    }
}
