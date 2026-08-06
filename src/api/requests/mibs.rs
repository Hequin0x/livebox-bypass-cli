use super::{ApiRequestSpec, EmptyParameters};
use crate::api::responses::mibs::MibsResponse;

#[derive(Debug)]
pub struct GetMibs;

impl ApiRequestSpec for GetMibs {
    const SERVICE: &'static str = "NeMo.Intf.data";
    const METHOD: &'static str = "getMIBs";

    type Parameters = EmptyParameters;
    type Response = MibsResponse;

    fn parameters(&self) -> Self::Parameters {
        EmptyParameters::default()
    }
}
